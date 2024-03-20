package flame.installer

import flame.SmeController
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.routing.Routing
import kase.response.get
import koncurrent.later.andThen
import koncurrent.later.await
import sentinel.bearerToken

fun Routing.installSme(controller: SmeController) {
    get(controller.routes.load(), controller.codec) {
        val scope = call.request.header(controller.resolver) ?: throw IllegalArgumentException("No scope provided")
        controller.auth(scope).session(token = bearerToken()).andThen {
            controller.sme(it).load()
        }.await()
    }
    installSmeAdmin(controller)
    installSmeFunding(controller)
    installSmeFinance(controller)
    installSmeDocuments(controller)
    installSmeGovernance(controller)
    installSmeSwot(controller)
}