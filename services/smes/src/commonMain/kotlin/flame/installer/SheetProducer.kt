@file:Suppress("Since15")

package flame.installer

import flame.sheet.SmeSheet
import flame.sheet.SmeSheetCell
import flame.sheet.SmeSheetCellAlign
import flame.sheet.SmeSheetRow
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.formula.eval.ErrorEval
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


class SheetProducer(val file: File) {

    fun produce():SmeSheet {
        val workbook = loadWorkbook(file)
        val sheet = workbook.getSheetAt(0)

        val evaluator = workbook.creationHelper.createFormulaEvaluator()
        evaluator.evaluateAll()

        val rows = mutableListOf<SmeSheetRow>()
        val merges = sheet.mergedRegions
        merges?.let { merges->
            merges.forEach { range->
                range?.formatAsString()
            }
        }
        val doneMerges:ArrayList<String> = ArrayList()
        for (rowIdx in 0..sheet.lastRowNum) {
            sheet.getRow(rowIdx)?.let { row->
                val cells = mutableListOf<SmeSheetCell>()
                var c = 0

                for (colIdx in 0..row.lastCellNum) {
                    row.getCell(colIdx)?.let { cell->
                        val font = cell.cellStyle?.let {
                            workbook.getFontAt(it.fontIndex)
                        }

                        val merge = merges.let { merges->
                            merges.mapNotNull {
                                it
                            }.firstOrNull {
                                it.isInRange(rowIdx, colIdx)
                            }
                        } ?: null

                        if (merge != null && doneMerges.contains(merge.formatAsString())) {

                        } else {
                            if (merge != null) {
                                doneMerges.add(merge.formatAsString());
                            }
                            cells.add(c, cell.toCell(font, merge, evaluator))
                            c++
                        }
                    }
                }

                rows.add(SmeSheetRow(
                    cells = cells
                ))
            }
        }

        return SmeSheet(
            rows = rows
        )
    }

    private fun loadWorkbook(file: File):Workbook {
        var workbook: Workbook? = null

        try {
            val excelFile: InputStream = FileInputStream(file)
            workbook = HSSFWorkbook(excelFile)
        } catch (e: Exception) {
        }

        if (workbook == null) {
            try {
                val excelFile: InputStream = FileInputStream(file)
                workbook = XSSFWorkbook(excelFile)
            } catch (e: Exception) {
            }
        }

        if (workbook == null) {
            return HSSFWorkbook()
        }

        return workbook
    }

    private fun format(cell:Cell, value:Any):String {
        val f = DataFormatter()
        val format = f.createFormat(cell)
        if (format != null) {
            var strVal = format.format(value)
            if (format is ExcelStyleDateFormatter) {
                strVal = strVal.replace("\\\"".toRegex(), "")
            }

            return strVal
        }

        return "$value"
    }

    private fun CellValue.error():String {
        val errorCode: Byte = this.errorValue

        // Get the corresponding error message
        val errorMessage = FormulaError.forInt(errorCode).string

        return errorMessage
    }

    private fun Cell.error():String {
        val errorCode: Byte = this.getErrorCellValue()

        // Get the corresponding error message
        val errorMessage = FormulaError.forInt(errorCode).string

        return errorMessage
    }

    private fun Cell.toCell(font:Font?, merge:CellRangeAddress?, evaluator:FormulaEvaluator):SmeSheetCell {
        val value = evaluator.evaluate(this)

        val cellValue:String = value?.cellType?.let {
            when (it) {
                CellType._NONE -> ""
                CellType.NUMERIC -> format(this, value.numberValue)
                CellType.STRING -> value.stringValue
                CellType.FORMULA -> "</>"
                CellType.BLANK -> ""
                CellType.BOOLEAN -> format (this, value.booleanValue)
                CellType.ERROR -> this.error()
            }
        }?: this.cellType?.let {
            when (it) {
                CellType._NONE -> ""
                CellType.NUMERIC -> format(this, this.numericCellValue)
                CellType.STRING -> this.stringCellValue
                CellType.FORMULA -> "</>"
                CellType.BLANK -> ""
                CellType.BOOLEAN -> format(this, this.booleanCellValue)
                CellType.ERROR -> this.error()
            }
        } ?: ""

        return SmeSheetCell(
            content = cellValue,
            bold = font?.bold ?: false,
            indent = false,
            align = this.cellStyle.alignment?.let { hAlign->
                when (hAlign) {
                    HorizontalAlignment.GENERAL -> SmeSheetCellAlign.left
                    HorizontalAlignment.LEFT -> SmeSheetCellAlign.left
                    HorizontalAlignment.CENTER -> SmeSheetCellAlign.center
                    HorizontalAlignment.RIGHT -> SmeSheetCellAlign.right
                    HorizontalAlignment.FILL -> SmeSheetCellAlign.left
                    HorizontalAlignment.JUSTIFY -> SmeSheetCellAlign.justify
                    HorizontalAlignment.CENTER_SELECTION -> SmeSheetCellAlign.center
                    HorizontalAlignment.DISTRIBUTED -> SmeSheetCellAlign.left
                }
            }?: SmeSheetCellAlign.left,
            rowspan = merge?.let { merge->
                merge.lastRow - merge.firstRow + 1
            } ?: 1,
            colspan = merge?.let {
                merge.lastColumn - merge.firstColumn + 1
            } ?: 1,
        )
    }
}