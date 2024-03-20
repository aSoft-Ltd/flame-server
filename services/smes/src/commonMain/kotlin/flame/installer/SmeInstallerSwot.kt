package flame.installer

import flame.SmeController
import flame.SmeKey
import flame.SmeService
import io.ktor.server.application.call
import io.ktor.server.request.header
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
        val scope = call.request.header(controller.resolver) ?: throw IllegalArgumentException("No scope provided")
        controller.auth(scope).session(token = bearerToken()).then {
            controller.sme(it)
        }.andThen { service ->
            service(key).update(params)
        }.await()
    }
}

private operator fun SmeService.invoke(key: SmeKey.Swot) = when (key) {
    SmeKey.Swot.strengths -> swot.strengths
    SmeKey.Swot.weaknesses -> swot.weaknesses
    SmeKey.Swot.opportunities -> swot.opportunities
    SmeKey.Swot.threats -> swot.threats
}