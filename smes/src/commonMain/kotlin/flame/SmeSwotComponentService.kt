package flame

import koncurrent.Later
import sentinel.Sessioned

interface SmeSwotComponentService {
    fun save(params: Sessioned<List<String>>) : Later<SmeDto>
}