package flame

import flame.finance.SmeBackOfficeDto
import flame.finance.SmeFinancialAcquisitionDto
import flame.finance.SmeFinancialStatusDto
import koncurrent.Later
import koncurrent.later.then
import koncurrent.later.andThen
import koncurrent.later.andZip
import koncurrent.later.zip
import koncurrent.later.catch
import sentinel.Sessioned

interface SmeFinanceService {
    fun saveOffice(params: Sessioned<SmeBackOfficeDto>) : Later<SmeDto>

    fun saveStatus(params: Sessioned<SmeFinancialStatusDto>) : Later<SmeDto>

    fun saveAcquisition(params: Sessioned<SmeFinancialAcquisitionDto>): Later<SmeDto>
}