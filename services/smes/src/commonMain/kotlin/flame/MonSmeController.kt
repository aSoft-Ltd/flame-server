package flame

import kotlinx.serialization.StringFormat
import sentinel.EmailAuthenticationService
import sentinel.UserSession

class MonSmeController(
    val auth: EmailAuthenticationService,
    val sme: (UserSession) -> MonSmeScheme,
    val resolver: String,
    val routes: MonSmeReference,
    val codec: StringFormat
)