package flame

interface SmeSwotService : SmeSwotScheme {
    override val strengths: SmeSwotComponentService
    override val weaknesses: SmeSwotComponentService
    override val opportunities: SmeSwotComponentService
    override val threats: SmeSwotComponentService
}