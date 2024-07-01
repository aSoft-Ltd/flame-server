package flame.daos

import java.time.LocalDate

data class ReportDateDao(
    val start: LocalDate,
    val end: LocalDate
)