package flame.daos

import flame.daos.admin.SmeAdminDao
import flame.finance.SmeFinanceDto
import flame.funding.SmeFundingDto
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class SmeDao(
    @BsonId
    val uid: ObjectId? = null,
    val company: ObjectId,
    val admin: SmeAdminDao = SmeAdminDao(),
    val funding: SmeFundingDto = SmeFundingDto(),
    val finance: SmeFinanceDto = SmeFinanceDto()
)