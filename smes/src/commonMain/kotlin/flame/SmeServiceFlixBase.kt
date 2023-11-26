package flame

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import flame.daos.SmeDao
import flame.transformers.toDto
import koncurrent.Later
import koncurrent.later
import kotlin.reflect.KProperty
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId
import sentinel.Sessioned

abstract class SmeServiceFlixBase(protected val options: SmeServiceOptions) {

    protected val logger by options.logger

    protected fun <T> Sessioned<T>.save(key: SmeKey, vararg props: KProperty<*>): Later<SmeDto> = options.scope.later {
        val data = arrayOf(
            "user" to "${session.user.name} (${session.user.uid})",
            "company" to "${session.company.name} (${session.company.uid})",
        )
        val tracer = logger.trace(options.message.save(key), *data)
        val qualifier = props.joinToString(".") { it.name }
        val matcher = Filters.eq(SmeDao::company.name, ObjectId(session.company.uid))
        val update = Updates.set(qualifier, params)
        options.col.updateOne(matcher, update)
        options.col.find<SmeDao>(matcher).firstOrNull().toDto().also { tracer.passed() }
    }
}