package flame

import koncurrent.Later
import sentinel.UserSession

interface SmeService : SmeScheme {

    override val admin: SmeAdminService

    override val funding: SmeFundingService

    override val finance: SmeFinanceService

    override val documents: SmeDocumentsService

    override val governance: SmeGovernanceService

    override val swot: SmeSwotService
}