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

class SmeFinanceServiceFlix(options: SmeServiceOptions) : SmeServiceFlixBase(options), SmeFinanceService {

    private fun <T> T.save(key: SmeKey, prop: KProperty<*>): Later<SmeDto> = save(key, SmeDao::finance, prop)

    override fun update(params: SmeBackOfficeDto): Later<SmeDto> = params.save(SmeKey.Finance.office, SmeFinanceDto::office)

    override fun update(params: SmeFinancialStatusDto): Later<SmeDto> = params.save(SmeKey.Finance.status, SmeFinanceDto::status)

    override fun update(params: SmeFinancialAcquisitionDto): Later<SmeDto> = params.save(SmeKey.Finance.acquisition, SmeFinanceDto::acquisition)
}