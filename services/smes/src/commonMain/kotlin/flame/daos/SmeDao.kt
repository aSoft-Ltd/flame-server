package flame.daos

import cabinet.Attachment
import flame.daos.admin.SmeAdminDao
import flame.daos.swot.SmeSwotDao
import flame.finance.SmeFinanceDto
import flame.funding.SmeFundingDto
import flame.governance.SmeGovernanceDto
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class SmeDao(
    @BsonId
    val uid: ObjectId? = null,
    val company: ObjectId,
    val admin: SmeAdminDao? = null,
    val funding: SmeFundingDto? = null,
    val finance: SmeFinanceDto? = null,
    val documents: List<Attachment> = emptyList(),
    val governance: SmeGovernanceDto? = null,
    val swot: SmeSwotDao? = null
)