package flame

import kotlinx.serialization.StringFormat
import sentinel.EmailAuthenticationService
import sentinel.UserSession

class SmeController(
    val auth: suspend (domain: String) -> EmailAuthenticationService,
    val sme: (UserSession) -> SmeService,
    val resolver: String,
    val routes: SmeEndpoint,
    val codec: StringFormat
)