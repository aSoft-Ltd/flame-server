package flame.transformers.swot

import flame.daos.swot.SmeSwotDao
import flame.swot.SmeSwotDto

internal fun SmeSwotDto.toDao() = SmeSwotDao(
    strengths = strengths.ifEmpty { null },
    weaknesses = weaknesses.ifEmpty { null },
    opportunities = opportunities.ifEmpty { null },
    threats = threats.ifEmpty { null },
)

internal fun SmeSwotDao.toDto() = SmeSwotDto(
    strengths = strengths ?: emptyList(),
    weaknesses = weaknesses ?: emptyList(),
    opportunities = opportunities ?: emptyList(),
    threats = threats ?: emptyList(),
)