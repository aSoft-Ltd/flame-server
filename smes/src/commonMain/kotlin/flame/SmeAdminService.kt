package flame

import koncurrent.Later
import sentinel.Sessioned

interface SmeAdminService : SmeAdminScheme {
    fun saveContacts(params: Sessioned<SmeContactsDto>) : Later<SmeDto>

    fun saveBusiness(params: Sessioned<SmeBusinessDto>) : Later<SmeDto>

    fun saveLegal(params: Sessioned<SmeLegalComplianceDto>) : Later<SmeDto>

    fun saveShareholders(params: Sessioned<List<SmeShareholderDto>>) : Later<SmeDto>

    fun saveDirectors(params: Sessioned<List<SmeDirectorDto>>) : Later<SmeDto>
}