package flame

import kotlinx.serialization.StringFormat
import sentinel.EmailAuthenticationService
import sentinel.UserSession

class OwnSmeController(
    val auth: suspend (domain: String) -> EmailAuthenticationService,
    val sme: (UserSession) -> OwnSmeScheme,
    val resolver: String,
    val routes: OwnSmeReference,
    val codec: StringFormat,
)