package flame.transformers

import flame.SmeDto
import flame.daos.SmeDao

internal fun SmeDao?.toDto() = SmeDto(
    uid = this?.uid?.toHexString() ?: throw IllegalArgumentException("Can't transform an SME dao with a null id"),
    admin = null
)