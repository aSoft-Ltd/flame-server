package flame.installer

import flame.SmeMonitorController
import io.ktor.server.routing.Routing
import kase.response.get
import koncurrent.later.andThen
import koncurrent.later.await
import sentinel.bearerToken

fun Routing.installSmes(controller: SmeMonitorController) {
    get(controller.routes.smes(), controller.codec) {
        controller.auth.session(
            token = bearerToken()
        ).andThen {
            controller.smes(it).list()
        }.await()
    }
}