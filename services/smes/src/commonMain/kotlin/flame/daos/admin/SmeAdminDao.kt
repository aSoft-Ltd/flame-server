package flame.daos.admin

import flame.admin.SmeBusinessDto
import flame.admin.SmeDirectorDto
import flame.admin.SmeLegalComplianceDto
import flame.admin.SmeShareholderDto

data class SmeAdminDao(
    val contacts: SmeContactsDao? = null,
    val business: SmeBusinessDto? = null,
    val legal: SmeLegalComplianceDto? = null,
    val directors: List<SmeDirectorDto>? = null,
    val shareholders: List<SmeShareholderDto>? = null,
)