package flame

import flame.governance.SmeGovernanceDto
import koncurrent.Later
import koncurrent.later.then
import koncurrent.later.andThen
import koncurrent.later.andZip
import koncurrent.later.zip
import koncurrent.later.catch
import sentinel.Sessioned

interface SmeGovernanceService {
    fun saveGovernance(params: Sessioned<SmeGovernanceDto>) : Later<SmeDto>
}