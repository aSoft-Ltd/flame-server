package flame

import flame.admin.SmeBusinessDto
import flame.admin.SmeContactsDto
import flame.admin.SmeDirectorDto
import flame.admin.SmeLegalComplianceDto
import flame.admin.SmeShareholderDto
import koncurrent.Later
import koncurrent.later.then
import koncurrent.later.andThen
import koncurrent.later.andZip
import koncurrent.later.zip
import koncurrent.later.catch
import sentinel.Sessioned

interface SmeAdminService : SmeAdminScheme {
    fun saveContacts(params: Sessioned<SmeContactsDto>) : Later<SmeDto>

    fun saveBusiness(params: Sessioned<SmeBusinessDto>) : Later<SmeDto>

    fun saveLegal(params: Sessioned<SmeLegalComplianceDto>) : Later<SmeDto>

    fun saveShareholders(params: Sessioned<List<SmeShareholderDto>>) : Later<SmeDto>

    fun saveDirectors(params: Sessioned<List<SmeDirectorDto>>) : Later<SmeDto>
}