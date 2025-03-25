package flame

import com.mongodb.client.model.Filters
import flame.admin.SmeBusinessDto
import flame.daos.SmeDao
import flame.daos.admin.SmeAdminDao
import flame.transformers.toDao
import flame.transformers.toDto
import koncurrent.Later
import koncurrent.later
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.future.await
import kronecker.LoadOptions
import org.bson.types.ObjectId

class MonSmeServiceFlix(
    private val options: SmeServiceFlixOptions,
) : MonSmeScheme {

    private val scope = options.scope
    private val col = options.col
    private val session = options.session

    override fun create(params: SmeBusinessDto): Later<SmeDto> = scope.later {
        val dao = SmeDao(
            company = ObjectId(options.session.company.uid),
            admin = SmeAdminDao(business = params)
        )
        val id = col.insertOne(dao)
        load(id.insertedId.toString()).await()
    }

    override fun list(options: LoadOptions): Later<List<SmeDto>> = scope.later {
        val company = ObjectId(session.company.uid)
//        val applications = applications.find(Filters.eq(SmeDao::scope.name, company)).skip(ignore).limit(options.limit).toList()
//        col.find<SmeDao>(Filters.eq(SmeDao::company.name, company)).skip((options.page - 1) * options.limit).limit(options.limit).map {
//            it.toDto()
//        }.toList()

        col.find<SmeDao>().skip((options.page - 1) * options.limit).limit(options.limit).map {
            it.toDto()
        }.toList()
    }

    override fun load(uid: String): Later<SmeDto> = scope.later {
        val dao = col.find<SmeDao>(Filters.eq("_id", ObjectId(uid))).firstOrNull() ?: run {
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