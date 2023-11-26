package flame

import com.mongodb.client.model.Filters.eq
import flame.daos.SmeDao
import flame.transformers.toDto
import koncurrent.Later
import koncurrent.later
import koncurrent.later.await
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId
import sentinel.UserSession

class SmeServiceFlix(private val options: SmeServiceOptions) : SmeService {

    private val logger by options.logger

    override val admin by lazy { SmeAdminServiceFlix(options) }

    override val funding by lazy { SmeFundingServiceFlix(options) }

    override val finance by lazy { SmeFinanceServiceFlix(options) }
    override fun load(session: UserSession): Later<SmeDto> = options.scope.later {
        val trace = logger.trace(options.message.load())
        val found = options.col.find<SmeDao>(eq(SmeDao::company.name, ObjectId(session.company.uid))).firstOrNull()
        if (found == null) {
            options.col.insertOne(SmeDao(company = ObjectId(session.company.uid)))
            load(session).await()
        } else found.toDto().also { trace.passed() }
    }
}