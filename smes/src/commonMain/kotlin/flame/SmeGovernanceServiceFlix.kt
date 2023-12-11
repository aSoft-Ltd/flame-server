package flame

import flame.daos.SmeDao
import flame.finance.SmeBackOfficeDto
import flame.finance.SmeFinanceDto
import flame.finance.SmeFinancialAcquisitionDto
import flame.finance.SmeFinancialStatusDto
import flame.governance.SmeGovernanceDto
import koncurrent.Later
import kotlin.reflect.KProperty
import sentinel.Sessioned

class SmeGovernanceServiceFlix(options: SmeServiceOptions) : SmeServiceFlixBase(options), SmeGovernanceService {
    override fun saveGovernance(params: Sessioned<SmeGovernanceDto>) = params.save(SmeKey.Governance, SmeDao::governance)
}