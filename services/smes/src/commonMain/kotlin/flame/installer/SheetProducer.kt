@file:Suppress("Since15")

package flame.installer

import flame.sheet.SmeSheet
import flame.sheet.SmeSheetCell
import flame.sheet.SmeSheetRow
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class SheetProducer(val file: File) {

    fun produce():SmeSheet {
        val workbook = loadWorkbook(file)
        val sheet = workbook.getSheetAt(0)
        val rows = mutableListOf<SmeSheetRow>()
        sheet.rowIterator().forEach {row->
            val cells = mutableListOf<SmeSheetCell>()
            var c = 0
            row.cellIterator().forEach { cell->
                cells.add(c, SmeSheetCell(
                    content = cell.stringCellValue
                ))
                c++
            }
            rows.add(SmeSheetRow(
                cells = cells
            ))
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
}