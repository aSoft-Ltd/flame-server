package flame.daos.admin

import flame.admin.SmeBusinessDto
import flame.governance.SmeDirectorDto
import flame.admin.SmeLegalComplianceDto
import flame.admin.SmeShareholderDto

data class SmeAdminDao(
    val contacts: SmeContactsDao? = null,
    val business: SmeBusinessDto? = null,
    val legal: SmeLegalComplianceDto? = null,
    val shareholders: List<SmeShareholderDto>? = null,
)