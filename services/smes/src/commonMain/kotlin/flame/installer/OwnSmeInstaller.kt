package flame.installer

import flame.OwnSmeController
import flame.SmeDto
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.request.receiveText
import io.ktor.server.routing.Routing
import kase.response.get
import kase.response.patch
import koncurrent.later.andThen
import koncurrent.later.await
import kotlinx.serialization.decodeFromString
import sentinel.bearerToken

fun Routing.installOwnSme(controller: OwnSmeController) {
    get(controller.routes.load(), controller.codec) {
        val scope = call.request.header(controller.resolver) ?: throw IllegalArgumentException("No scope provided")
        controller.auth(scope).session(token = bearerToken()).andThen {
            controller.sme(it).load()
        }.await()
    }

    patch(controller.routes.update(), controller.codec) {
        val scope = call.request.header(controller.resolver) ?: throw IllegalArgumentException("No scope provided")
        val params = controller.codec.decodeFromString<SmeDto>(call.receiveText())
        controller.auth(scope).session(token = bearerToken()).andThen {
            controller.sme(it).update(params)
        }.await()
    }
}