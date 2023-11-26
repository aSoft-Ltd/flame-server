package flame.daos.admin

import flame.admin.SmeBusinessDto
import flame.admin.SmeDirectorDto
import flame.admin.SmeLegalComplianceDto
import flame.admin.SmeShareholderDto

data class SmeAdminDao(
    val contacts: SmeContactsDao = SmeContactsDao(),
    val business: SmeBusinessDto = SmeBusinessDto(),
    val legal: SmeLegalComplianceDto = SmeLegalComplianceDto(),
    val directors: List<SmeDirectorDto> = emptyList(),
    val shareholders: List<SmeShareholderDto> = emptyList(),
)