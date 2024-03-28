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
    private val options: SmeServiceFlixOptions
) : OwnSmeScheme {

    private val scope = options.scope
    private val col = options.col

    override fun load(): Later<SmeDto> = scope.later {
        val uid = options.session.company.uid
        val dao = col.find<SmeDao>(Filters.eq(SmeDao::company.name, ObjectId(uid))).firstOrNull() ?: run {
            throw IllegalArgumentException("Sme(uid = $uid) does not exist")
        }
        dao.toDto()
    }

    override fun update(sme: SmeDto): Later<SmeDto> = scope.later {
        col.deleteOne(Filters.eq(SmeDao::uid.name, ObjectId(sme.uid)))
        col.insertOne(sme.toDao())
        sme
    }
}