package flame

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import flame.admin.SmeAdminDto
import flame.admin.SmeBusinessDto
import flame.admin.SmeContactsDto
import flame.admin.SmeDirectorDto
import flame.admin.SmeLegalComplianceDto
import flame.admin.SmeShareholderDto
import flame.daos.SmeDao
import flame.transformers.toDto
import koncurrent.Later
import koncurrent.later
import kotlin.reflect.KProperty
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId
import sentinel.Sessioned
import sentinel.UserSession

class SmeAdminServiceFlix2(val session: UserSession, options: SmeServiceOptions) : SmeServiceFlixBase(options), SmeAdminService {

    private fun <T> Sessioned<T>.save(key: SmeKey, prop: KProperty<*>): Later<SmeDto> = save(key, SmeDao::admin, prop)

    protected fun <T> T.save(key: SmeKey, vararg props: KProperty<*>): Later<SmeDto> = options.scope.later {
        val data = arrayOf(
            "user" to "${session.user.name} (${session.user.uid})",
            "company" to "${session.company.name} (${session.company.uid})",
        )
        val tracer = logger.trace(options.message.save(key), *data)
        val qualifier = props.joinToString(".") { it.name }
        val matcher = Filters.eq(SmeDao::company.name, ObjectId(session.company.uid))
        val update = Updates.set(qualifier, this@save)
        options.col.updateOne(matcher, update)
        options.col.find<SmeDao>(matcher).firstOrNull().toDto().also { tracer.passed() }
    }
    override fun saveContacts(params: Sessioned<SmeContactsDto>) = params.save(SmeKey.Admin.contacts, SmeAdminDto::contacts)

    override fun saveBusiness(params: Sessioned<SmeBusinessDto>) = params.save(SmeKey.Admin.businesses, SmeAdminDto::business)

    override fun saveLegal(params: Sessioned<SmeLegalComplianceDto>) = params.save(SmeKey.Admin.legal, SmeAdminDto::legal)

    override fun saveShareholders(params: Sessioned<List<SmeShareholderDto>>) = params.save(SmeKey.Admin.shareholders, SmeAdminDto::shareholders)

    override fun saveDirectors(params: Sessioned<List<SmeDirectorDto>>) = params.save(SmeKey.Admin.directors, SmeAdminDto::directors)
}