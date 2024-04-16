package flame

import com.mongodb.client.model.Filters
import flame.daos.SmeDao
import flame.transformers.toDao
import flame.transformers.toDto
import koncurrent.Later
import koncurrent.later
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId

class OwnSmeServiceFlix(
    private val options: OwnSmeServiceFlixOptions
) : OwnSmeScheme {

    private val scope = options.scope
    private val col = options.collection
    private val message = options.message
    private val logger by options.logger

    override fun load(): Later<SmeDto> = scope.later {
        val tracer = logger.trace(message.load())
        val uid = options.session.company.uid
        val dao = col.find<SmeDao>(Filters.eq(SmeDao::company.name, ObjectId(uid))).firstOrNull() ?: run {
            throw IllegalArgumentException("Sme(company = $uid) does not exist").also { tracer.failed(it) }
        }
        dao.toDto().also { tracer.passed() }
    }

    override fun update(sme: SmeDto): Later<SmeDto> = scope.later {
        val tracer = logger.trace(message.update())
        col.deleteOne(Filters.eq("_id", ObjectId(sme.uid)))
        col.insertOne(sme.toDao())
        sme.also { tracer.passed() }
    }
}