package flame

import flame.daos.SmeDao
import flame.finance.SmeBackOfficeDto
import flame.finance.SmeFinanceDto
import koncurrent.Later
import kotlin.reflect.KProperty
import sentinel.Sessioned

class SmeFinanceServiceFlix(options: SmeServiceOptions) : SmeServiceFlixBase(options), SmeFinanceService {

    private fun <T> Sessioned<T>.save(key: SmeKey, prop: KProperty<*>): Later<SmeDto> = save(key, SmeDao::finance, prop)

    override fun saveOffice(params: Sessioned<SmeBackOfficeDto>) = params.save(SmeKey.Finance.office, SmeFinanceDto::office)
}