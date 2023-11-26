package flame.daos.admin

import java.time.LocalDate

data class SmeContactsDao(
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val role: String? = null,
    val dob: LocalDate? = null,
)