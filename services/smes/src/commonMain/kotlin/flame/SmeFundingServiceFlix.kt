package flame

import flame.daos.SmeDao
import flame.funding.SmeAcquisitionDto
import flame.funding.SmeBreakdownDto
import flame.funding.SmeFundingDto
import flame.funding.SmeInvestmentDto
import koncurrent.Later
import kotlin.reflect.KProperty

class SmeFundingServiceFlix(options: SmeServiceOptions) : SmeServiceFlixBase(options), SmeFundingService {

    private fun <T> T.save(key: SmeKey, prop: KProperty<*>): Later<SmeDto> = save(key, SmeDao::funding, prop)

    override fun update(params: SmeInvestmentDto): Later<SmeDto> = params.save(SmeKey.Funding.investment, SmeFundingDto::investment)

    override fun update(params: SmeBreakdownDto): Later<SmeDto> = params.save(SmeKey.Funding.breakdown, SmeFundingDto::breakdown)

    override fun update(params: SmeAcquisitionDto): Later<SmeDto> = params.save(SmeKey.Funding.acquisition, SmeFundingDto::acquisition)
}