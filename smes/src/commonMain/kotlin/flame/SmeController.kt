package flame

import kotlinx.serialization.StringFormat
import sentinel.AuthenticationService

class SmeController(
    val auth: AuthenticationService,
    val sme: SmeService,
    val routes: SmeEndpoint,
    val codec: StringFormat
)