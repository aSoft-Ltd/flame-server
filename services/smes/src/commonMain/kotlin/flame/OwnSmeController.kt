package flame

import kotlinx.serialization.StringFormat
import sentinel.EmailAuthenticationService
import sentinel.UserSession

class OwnSmeController(
    val auth: suspend (domain: String) -> EmailAuthenticationService,
    val supervisor: suspend (domain: String) -> String,
    val directory: String,
    val sme: (UserSession) -> OwnSmeScheme,
    val resolver: String,
    val routes: OwnSmeReference,
    val codec: StringFormat,
)