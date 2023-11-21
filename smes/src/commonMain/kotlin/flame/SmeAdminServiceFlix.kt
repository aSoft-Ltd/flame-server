package flame

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import flame.daos.SmeDao
import flame.transformers.toDto
import koncurrent.Later
import koncurrent.later
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId
import sentinel.Sessioned

class SmeAdminServiceFlix(private val options: SmeServiceOptions) : SmeAdminService {

    private val logger by options.logger
    override fun saveContacts(params: Sessioned<SmeContactsDto>): Later<SmeDto> = options.scope.later {
        val tracer = logger.trace(options.message.saveContacts())
        val qualifier = listOf(SmeDao::admin.name, SmeAdminDto::contacts.name).joinToString(".")
        val matcher = Filters.eq(SmeDao::company.name, ObjectId(params.session.company.uid))
        val update = Updates.set(qualifier, params.params)
        options.col.updateOne(matcher, update)
        options.col.find<SmeDao>(matcher).firstOrNull().toDto().also { tracer.passed() }
    }
}