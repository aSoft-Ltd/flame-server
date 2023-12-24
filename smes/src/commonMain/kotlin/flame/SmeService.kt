package flame

import koncurrent.Later
import koncurrent.later.then
import koncurrent.later.andThen
import koncurrent.later.andZip
import koncurrent.later.zip
import koncurrent.later.catch
import sentinel.UserSession

interface SmeService : SmeScheme {

    override val admin: SmeAdminService

    override val funding: SmeFundingService

    val finance: SmeFinanceService

    val document: SmeDocumentService

    val governance: SmeGovernanceService

    val swot: SmeSwotService
    fun load(session: UserSession) : Later<SmeDto>
}