package flame

import flame.swot.SmeSwotDto

class SmeSwotServiceFlix(options: SmeServiceOptions) : SmeSwotService {
    override val strengths by lazy { SmeSwotComponentServiceFlix(options, SmeKey.Swot.strengths, SmeSwotDto::strengths) }
    override val weaknesses by lazy { SmeSwotComponentServiceFlix(options, SmeKey.Swot.weaknesses, SmeSwotDto::weaknesses) }
    override val opportunities by lazy { SmeSwotComponentServiceFlix(options, SmeKey.Swot.opportunities, SmeSwotDto::opportunities) }
    override val threats by lazy { SmeSwotComponentServiceFlix(options, SmeKey.Swot.threats, SmeSwotDto::threats) }
}