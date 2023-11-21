package flame

import com.mongodb.kotlin.client.coroutine.MongoClient
import flame.daos.SmeDao
import kotlinx.coroutines.CoroutineScope
import lexi.LoggerFactory

class SmeServiceOptions(
    val client: MongoClient,
    val logger: LoggerFactory,
    val scope: CoroutineScope
) {
    internal val message by lazy { SmeActionMessage() }

    internal val db by lazy {
        client.getDatabase("sme_info")
    }

    internal val col by lazy {
        db.getCollection<SmeDao>("smes")
    }
}