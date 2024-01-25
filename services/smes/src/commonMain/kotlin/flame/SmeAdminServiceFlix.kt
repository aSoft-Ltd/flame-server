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
import koncurrent.later.then
import koncurrent.later.andThen
import koncurrent.later.andZip
import koncurrent.later.zip
import koncurrent.later.catch
import koncurrent.later
import kotlin.reflect.KProperty
import sentinel.Sessioned

class SmeAdminServiceFlix(options: SmeServiceOptions) : SmeServiceFlixBase(options), SmeAdminService {
    private fun <T> T.save(key: SmeKey, prop: KProperty<*>): Later<SmeDto> = save(key, SmeDao::admin, prop)

    override fun update(params: SmeContactsDto): Later<SmeDto> = params.save(SmeKey.Admin.contacts, SmeAdminDto::contacts)

    override fun update(params: SmeBusinessDto): Later<SmeDto> = params.save(SmeKey.Admin.businesses, SmeAdminDto::business)

    override fun update(params: SmeLegalComplianceDto): Later<SmeDto> = params.save(SmeKey.Admin.legal, SmeAdminDto::legal)

    override fun updateShareholders(params: List<SmeShareholderDto>): Later<SmeDto> = params.save(SmeKey.Admin.shareholders, SmeAdminDto::shareholders)

    override fun updateDirectors(params: List<SmeDirectorDto>): Later<SmeDto> = params.save(SmeKey.Admin.directors, SmeAdminDto::directors)
}