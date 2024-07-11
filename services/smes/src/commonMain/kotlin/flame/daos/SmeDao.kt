package flame.daos

import cabinet.AttachmentDto
import flame.analysis.FinancialReportsDto
import flame.daos.admin.SmeAdminDao
import flame.daos.swot.SmeSwotDao
import flame.finance.SmeFinanceDto
import flame.funding.SmeFundingDto
import flame.governance.SmeGovernanceDto
import flame.plan.SmePlanDto
import flame.sheet.SmeSheet
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class SmeDao(
    @BsonId
    val uid: ObjectId? = null,
    val company: ObjectId,
    val origin: String = "picapital",
    val admin: SmeAdminDao? = null,
    val funding: SmeFundingDto? = null,
    val finance: SmeFinanceDto? = null,
    val documents: List<AttachmentDto> = emptyList(),
    val xlsx: AttachmentDto? = null,
    val sheet: SmeSheet? = null,
    val governance: SmeGovernanceDto? = null,
    val business: SmePlanDto? = null,
    val swot: SmeSwotDao? = null,
    val reports: List<FinancialReportsDao>? = null
)