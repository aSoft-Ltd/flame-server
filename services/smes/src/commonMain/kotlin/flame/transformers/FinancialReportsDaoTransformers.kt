package flame.transformers

import flame.analysis.FinancialReportsDto
import flame.daos.FinancialReportsDao

internal fun FinancialReportsDao.toDto() = FinancialReportsDto(
    date = date.toDto(),
    sheet = sheet,
    income = income,
    cash = cash,
)

internal fun FinancialReportsDto.toDao() = FinancialReportsDao(
    date = date.toDao(),
    sheet = sheet,
    income = income,
    cash = cash,
)