package flame

import kotlinx.serialization.StringFormat
import sentinel.EmailAuthenticationService
import sentinel.UserSession

class SmeController(
    val auth: EmailAuthenticationService,
    val sme: (UserSession) -> SmeService,
    val routes: SmeEndpoint,
    val codec: StringFormat
)