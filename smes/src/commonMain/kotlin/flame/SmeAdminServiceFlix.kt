package flame

import flame.admin.SmeAdminDto
import flame.admin.SmeBusinessDto
import flame.admin.SmeContactsDto
import flame.admin.SmeDirectorDto
import flame.admin.SmeLegalComplianceDto
import flame.admin.SmeShareholderDto
import flame.daos.SmeDao
import flame.transformers.admin.toDao
import koncurrent.Later
import koncurrent.later
import kotlin.reflect.KProperty
import sentinel.Sessioned

class SmeAdminServiceFlix(options: SmeServiceOptions) : SmeServiceFlixBase(options), SmeAdminService {

    private fun <T> Sessioned<T>.save(key: SmeKey, prop: KProperty<*>): Later<SmeDto> = save(key, SmeDao::admin, prop)

    override fun saveContacts(params: Sessioned<SmeContactsDto>) = params.map { it.toDao() }.save(SmeKey.Admin.contacts, SmeAdminDto::contacts)

    override fun saveBusiness(params: Sessioned<SmeBusinessDto>) = params.save(SmeKey.Admin.businesses, SmeAdminDto::business)

    override fun saveLegal(params: Sessioned<SmeLegalComplianceDto>) = params.save(SmeKey.Admin.legal, SmeAdminDto::legal)

    override fun saveShareholders(params: Sessioned<List<SmeShareholderDto>>) = params.save(SmeKey.Admin.shareholders, SmeAdminDto::shareholders)

    override fun saveDirectors(params: Sessioned<List<SmeDirectorDto>>) = params.save(SmeKey.Admin.directors, SmeAdminDto::directors)
}