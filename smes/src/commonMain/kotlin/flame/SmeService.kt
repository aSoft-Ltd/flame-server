package flame

import koncurrent.Later
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