package flame.transformers

import flame.SmeDto
import flame.daos.SmeDao
import flame.plan.SmePlanDto
import flame.transformers.admin.toDao
import flame.transformers.admin.toDto
import flame.transformers.swot.toDao
import flame.transformers.swot.toDto
import org.bson.types.ObjectId

fun SmeDao?.toDto() = SmeDto(
    uid = this?.uid?.toHexString() ?: throw IllegalArgumentException("Can't transform an SME dao with a null id to dto"),
    company = company.toHexString(),
    origin = origin,
    admin = admin?.toDto(),
    funding = funding,
    finance = finance,
    governance = governance,
    documents = documents,
    business = business ?: SmePlanDto(),
    swot = swot?.toDto()
)

internal fun SmeDto.toDao() = SmeDao(
    uid = ObjectId(uid),
    company = ObjectId(company),
    origin = origin,
    admin = admin?.toDao(),
    governance = governance,
    funding = funding,
    finance = finance,
    documents = documents,
    business = business,
    swot = swot?.toDao()
)