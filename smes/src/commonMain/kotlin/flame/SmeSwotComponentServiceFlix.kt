package flame

import flame.daos.SmeDao
import flame.finance.SmeBackOfficeDto
import flame.finance.SmeFinanceDto
import flame.finance.SmeFinancialAcquisitionDto
import flame.finance.SmeFinancialStatusDto
import koncurrent.Later
import koncurrent.later.then
import koncurrent.later.andThen
import koncurrent.later.andZip
import koncurrent.later.zip
import koncurrent.later.catch
import kotlin.reflect.KProperty
import sentinel.Sessioned

class SmeSwotComponentServiceFlix(options: SmeServiceOptions, private val key: SmeKey, private val prop: KProperty<*>) : SmeServiceFlixBase(options), SmeSwotComponentService {

    override fun save(params: Sessioned<List<String>>): Later<SmeDto> = params.save(key, SmeDao::swot, prop)
}