package flame

import com.mongodb.kotlin.client.coroutine.MongoCollection
import flame.daos.SmeDao
import kotlinx.coroutines.CoroutineScope
import lexi.LoggerFactory
import sentinel.UserSession

class SmeServiceFlixOptions(
    val session: UserSession,
    val logger: LoggerFactory,
    val scope: CoroutineScope,
    val col: MongoCollection<SmeDao>
)