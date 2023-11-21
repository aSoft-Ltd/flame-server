package flame.daos

import flame.SmeAdminDto
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class SmeDao(
    @BsonId
    val uid: ObjectId? = null,
    val company: ObjectId,
    val admin: SmeAdminDto? = null
)