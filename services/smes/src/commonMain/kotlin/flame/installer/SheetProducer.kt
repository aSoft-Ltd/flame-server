@file:Suppress("Since15")

package flame.installer

import flame.sheet.SmeSheet
import flame.sheet.SmeSheetCell
import flame.sheet.SmeSheetRow
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class SheetProducer(val file: File) {

    fun produce():SmeSheet {
        val workbook = loadWorkbook(file)
        val sheet = workbook.getSheetAt(0)

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
                            cells.add(c, cell.toCell(font, merge))
                            c++
                        }
                    }
                }

                rows.add(SmeSheetRow(
                    cells = cells
                ))
            }
        }

//        sheet.rowIterator().forEach {row->
//            val cells = mutableListOf<SmeSheetCell>()
//            var c = 0
//            row.cellIterator().forEach { cell->
//                cells.add(c, SmeSheetCell(
//                    content = cell.stringCellValue
//                ))
//                c++
//            }
//            rows.add(SmeSheetRow(
//                cells = cells
//            ))
//        }

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

    private fun Cell.toCell(font:Font?, merge:CellRangeAddress?):SmeSheetCell {

        val cellValue = this.cellType?.let {
            when (it) {
                CellType._NONE -> ""
                CellType.NUMERIC -> "${this.numericCellValue}"
                CellType.STRING -> this.stringCellValue
                CellType.FORMULA -> "</>"
                CellType.BLANK -> ""
                CellType.BOOLEAN -> when (this.booleanCellValue) {
                    true -> "true"
                    false -> "false"
                }
                CellType.ERROR -> "Error"
            }
        }

        return SmeSheetCell(
            content = cellValue ?: "",
            bold = font?.bold ?: false,
            indent = false,
            colspan = merge?.let { merge->
                merge.lastRow - merge.firstRow + 1
            } ?: 1,
            rowspan = merge?.let {
                merge.lastColumn - merge.firstColumn + 1
            } ?: 1,
        )
    }
}