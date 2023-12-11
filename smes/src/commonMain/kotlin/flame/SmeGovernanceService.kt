package flame

import flame.governance.SmeGovernanceDto
import koncurrent.Later
import sentinel.Sessioned

interface SmeGovernanceService {
    fun saveGovernance(params: Sessioned<SmeGovernanceDto>) : Later<SmeDto>
}