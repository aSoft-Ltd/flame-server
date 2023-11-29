package flame.transformers

import flame.SmeDto
import flame.daos.SmeDao
import flame.daos.admin.SmeAdminDao
import flame.finance.SmeFinanceDto
import flame.funding.SmeFundingDto
import flame.transformers.admin.toDao
import flame.transformers.admin.toDto
import org.bson.types.ObjectId

internal fun SmeDao?.toDto() = SmeDto(
    uid = this?.uid?.toHexString() ?: throw IllegalArgumentException("Can't transform an SME dao with a null id"),
    company = company.toHexString(),
    admin = admin.toDto(),
    funding = funding,
    finance = finance,
    documents = documents,
)

inline fun SmeDto.toDao() = SmeDao(
    uid = ObjectId(uid),
    company = ObjectId(company),
    admin = admin?.toDao() ?: SmeAdminDao(),
    funding = funding ?: SmeFundingDto(),
    finance = finance ?: SmeFinanceDto(),
    documents = documents
)