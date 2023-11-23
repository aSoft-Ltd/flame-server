package flame.daos

import flame.admin.SmeAdminDto
import flame.finance.SmeFinanceDto
import flame.funding.SmeFundingDto
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class SmeDao(
    @BsonId
    val uid: ObjectId? = null,
    val company: ObjectId,
    val admin: SmeAdminDto? = null,
    val funding: SmeFundingDto? = null,
    val finance: SmeFinanceDto? = null
)