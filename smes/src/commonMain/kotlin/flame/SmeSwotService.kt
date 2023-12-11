package flame

interface SmeSwotService {
    val strengths: SmeSwotComponentService
    val weaknesses: SmeSwotComponentService
    val opportunities: SmeSwotComponentService
    val threats: SmeSwotComponentService
}