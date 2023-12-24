package flame

import flame.funding.SmeAcquisitionDto
import flame.funding.SmeBreakdownDto
import flame.funding.SmeInvestmentDto
import koncurrent.Later
import koncurrent.later.then
import koncurrent.later.andThen
import koncurrent.later.andZip
import koncurrent.later.zip
import koncurrent.later.catch
import sentinel.Sessioned

interface SmeFundingService : SmeFundingScheme {
    fun saveInvestment(params: Sessioned<SmeInvestmentDto>) : Later<SmeDto>

    fun saveBreakdown(params: Sessioned<SmeBreakdownDto>) : Later<SmeDto>

    fun saveAcquisition(params: Sessioned<SmeAcquisitionDto>) : Later<SmeDto>
}