package flame

import koncurrent.Later
import sentinel.Sessioned

interface SmeAdminService : SmeAdminScheme {
    fun saveContacts(params: Sessioned<SmeContactsDto>) : Later<SmeDto>
}