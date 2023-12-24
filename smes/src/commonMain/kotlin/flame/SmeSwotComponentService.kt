package flame

import koncurrent.Later
import koncurrent.later.then
import koncurrent.later.andThen
import koncurrent.later.andZip
import koncurrent.later.zip
import koncurrent.later.catch
import sentinel.Sessioned

interface SmeSwotComponentService {
    fun save(params: Sessioned<List<String>>) : Later<SmeDto>
}