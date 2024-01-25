package flame.transformers.admin

import flame.admin.SmeContactsDto
import flame.daos.admin.SmeContactsDao
import krono.toLocalDate

internal fun SmeContactsDto.toDao() = SmeContactsDao(
    firstName = firstName,
    lastName = lastName,
    email = email,
    phone = phone,
    role = role,
    dob = dob?.toLocalDate(),
)

internal fun SmeContactsDao.toDto() = SmeContactsDto(
    firstName = firstName,
    lastName = lastName,
    email = email,
    phone = phone,
    role = role,
    dob = dob?.toLocalDate()?.getOrNull(),
)