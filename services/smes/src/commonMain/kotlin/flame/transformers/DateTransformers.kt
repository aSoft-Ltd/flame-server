package flame.transformers

import flame.analysis.ReportDate
import flame.daos.ReportDateDao
import java.time.LocalDate

internal fun LocalDate.toDto() = krono.LocalDate(year, monthValue, dayOfMonth).getOrThrow()

internal fun krono.LocalDate.toDao() = LocalDate.of(year, month.number, dayOfMonth)

internal fun ReportDateDao.toDto() = ReportDate(
    start = start.toDto(),
    end = end.toDto()
)

internal fun ReportDate.toDao() = ReportDateDao(
    start = start.toDao(),
    end = end.toDao()
)