package flame

import kotlinx.serialization.StringFormat
import sentinel.EmailAuthenticationService
import sentinel.UserSession

class SmeMonitorController(
    val auth: EmailAuthenticationService,
    val smes: (UserSession) -> SmeMonitorService,
    val routes: SmeEndpoint,
    val codec: StringFormat
)