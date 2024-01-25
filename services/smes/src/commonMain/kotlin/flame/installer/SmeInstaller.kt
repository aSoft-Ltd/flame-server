package flame.installer

import flame.SmeController
import io.ktor.server.routing.Routing
import kase.response.get
import koncurrent.later.andThen
import koncurrent.later.await
import sentinel.bearerToken

fun Routing.installSme(controller: SmeController) {
    get(controller.routes.load(), controller.codec) {
        controller.auth.session(
            token = bearerToken()
        ).andThen {
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