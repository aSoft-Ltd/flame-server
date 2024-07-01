package flame.daos

import flame.analysis.BalanceSheetDto
import flame.analysis.CashFlowDto
import flame.analysis.IncomeStatementDto

data class FinancialReportsDao(
    val date: ReportDateDao,
    val sheet: BalanceSheetDto,
    val income: IncomeStatementDto,
    val cash: CashFlowDto,
)