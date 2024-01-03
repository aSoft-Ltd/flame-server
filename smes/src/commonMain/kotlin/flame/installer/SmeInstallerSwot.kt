package flame.installer

import flame.SmeController
import flame.SmeKey
import io.ktor.server.application.call
import io.ktor.server.request.receiveText
import io.ktor.server.routing.Routing
import kase.response.post
import koncurrent.later.andThen
import koncurrent.later.await
import koncurrent.later.then
import kotlinx.serialization.decodeFromString
import sentinel.Sessioned
import sentinel.bearerToken

internal fun Routing.installSmeSwot(controller: SmeController) = SmeKey.Swot.entries.forEach { key ->
    post(controller.routes.save(key), controller.codec) {
        val params = controller.codec.decodeFromString<List<String>>(call.receiveText())
        controller.auth.session(token = bearerToken()).then {
            Sessioned(it, params)
        }.andThen {
            controller.service(key).save(it)
        }.await()
    }
}

private fun SmeController.service(key: SmeKey.Swot) = when (key) {
    SmeKey.Swot.strengths -> sme.swot.strengths
    SmeKey.Swot.weaknesses -> sme.swot.weaknesses
    SmeKey.Swot.opportunities -> sme.swot.opportunities
    SmeKey.Swot.threats -> sme.swot.threats
}