package flame

import com.mongodb.client.model.Filters.eq
import flame.daos.SmeDao
import flame.transformers.toDto
import kollections.List
import koncurrent.Later
import koncurrent.TODOLater
import koncurrent.later
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kronecker.LoadOptions
import org.bson.types.ObjectId

class SmeMonitorServiceFlix(private val config: SmeServiceOptions) : SmeMonitorService {
    override fun list(options: LoadOptions): Later<List<SmeDto>> = config.scope.later {
        config.col.find<SmeDao>().skip((options.page - 1) * options.limit).limit(options.limit).map {
            it.toDto()
        }.toList()
    }

    override fun load(uid: String): Later<SmeDto> = config.scope.later {
        val dao = config.col.find<SmeDao>(eq(SmeDao::company.name, ObjectId(uid))).first()
        dao.toDto()
    }

    override fun update(sme: SmeDto): Later<SmeDto> = TODOLater()
}