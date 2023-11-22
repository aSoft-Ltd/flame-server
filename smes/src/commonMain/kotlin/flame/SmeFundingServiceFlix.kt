package flame

import flame.daos.SmeDao
import flame.funding.SmeFundingDto
import flame.funding.SmeInvestmentDto
import koncurrent.Later
import kotlin.reflect.KProperty
import sentinel.Sessioned

class SmeFundingServiceFlix(options: SmeServiceOptions) : SmeServiceFlixBase(options), SmeFundingService {

    private fun <T> Sessioned<T>.save(key: SmeKey, prop: KProperty<*>): Later<SmeDto> = save(key, SmeDao::funding, prop)


    override fun saveInvestment(params: Sessioned<SmeInvestmentDto>): Later<SmeDto> = params.save(SmeKey.Funding.investment,SmeFundingDto::investment)
}