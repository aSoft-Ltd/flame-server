package flame.daos.swot

data class SmeSwotDao(
    val strengths: List<String>? = null,
    val weaknesses: List<String>? = null,
    val opportunities: List<String>? = null,
    val threats: List<String>? = null,
)