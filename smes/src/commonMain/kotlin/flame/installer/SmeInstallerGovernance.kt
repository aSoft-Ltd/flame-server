package flame.installer

import flame.SmeController
import flame.SmeKey
import flame.governance.SmeGovernanceDto
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

internal fun Routing.installSmeGovernance(controller: SmeController) = post(controller.routes.save(SmeKey.Governance), controller.codec) {
    val params = controller.codec.decodeFromString<SmeGovernanceDto>(call.receiveText())
    controller.auth.session(token = bearerToken()).then {
        Sessioned(it, params)
    }.andThen {
        controller.sme.governance.saveGovernance(it)
    }.await()
}