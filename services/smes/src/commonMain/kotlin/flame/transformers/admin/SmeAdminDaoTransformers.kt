package flame.transformers.admin

import flame.admin.SmeAdminDto
import flame.admin.SmeBusinessDto
import flame.admin.SmeLegalComplianceDto
import flame.daos.admin.SmeAdminDao
import flame.daos.admin.SmeContactsDao
import kollections.emptyList

fun SmeAdminDto.toDao() = SmeAdminDao(
    contacts = contacts?.toDao() ?: SmeContactsDao(),
    business = business ?: SmeBusinessDto(),
    legal = legal ?: SmeLegalComplianceDto(),
    directors = directors,
    shareholders = shareholders,
)

fun SmeAdminDao.toDto() = SmeAdminDto(
    contacts = contacts?.toDto(),
    business = business,
    legal = legal,
    directors = directors ?: emptyList(),
    shareholders = shareholders ?: emptyList(),
)