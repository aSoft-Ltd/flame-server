package flame

import koncurrent.Later
import sentinel.UserSession

interface SmeService : SmeScheme {

    override val admin: SmeAdminService
    fun load(session: UserSession) : Later<SmeDto>
}