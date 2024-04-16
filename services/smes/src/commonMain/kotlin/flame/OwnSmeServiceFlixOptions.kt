package flame

import com.mongodb.kotlin.client.coroutine.MongoCollection
import flame.daos.SmeDao
import kotlinx.coroutines.CoroutineScope
import lexi.LoggerFactory
import sentinel.UserSession

class OwnSmeServiceFlixOptions(
    val session: UserSession,
    val logger: LoggerFactory,
    val message: OwnSmeReference,
    val scope: CoroutineScope,
    val collection: MongoCollection<SmeDao>
)