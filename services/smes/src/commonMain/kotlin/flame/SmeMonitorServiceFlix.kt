package flame

import flame.daos.SmeDao
import flame.transformers.toDto
import kollections.List
import koncurrent.Later
import koncurrent.later
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kronecker.LoadOptions

class SmeMonitorServiceFlix(private val config: SmeServiceOptions) : SmeMonitorService {
    override fun list(options: LoadOptions): Later<List<SmeDto>> = config.scope.later {
        config.col.find<SmeDao>().skip((options.page - 1) * options.limit).limit(options.limit).map {
            it.toDto()
        }.toList()
    }
}