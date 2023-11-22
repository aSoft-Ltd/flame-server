package flame

import flame.funding.SmeInvestmentDto
import koncurrent.Later
import sentinel.Sessioned

interface SmeFundingService : SmeFundingScheme {
    fun saveInvestment(params: Sessioned<SmeInvestmentDto>) : Later<SmeDto>
}