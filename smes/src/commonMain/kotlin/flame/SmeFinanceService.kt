package flame

import flame.finance.SmeBackOfficeDto
import flame.funding.SmeAcquisitionDto
import flame.funding.SmeBreakdownDto
import flame.funding.SmeInvestmentDto
import koncurrent.Later
import sentinel.Sessioned

interface SmeFinanceService {
    fun saveOffice(params: Sessioned<SmeBackOfficeDto>) : Later<SmeDto>
}