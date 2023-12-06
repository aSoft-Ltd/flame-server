package flame

import flame.finance.SmeBackOfficeDto
import flame.finance.SmeFinancialStatusDto
import koncurrent.Later
import sentinel.Sessioned

interface SmeFinanceService {
    fun saveOffice(params: Sessioned<SmeBackOfficeDto>) : Later<SmeDto>

    fun saveStatus(params: Sessioned<SmeFinancialStatusDto>) : Later<SmeDto>
}