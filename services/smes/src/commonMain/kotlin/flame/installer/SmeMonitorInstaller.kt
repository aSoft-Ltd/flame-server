package flame.installer

import flame.SmeMonitorController
import io.ktor.server.application.call
import io.ktor.server.routing.Routing
import io.ktor.server.util.getValue
import kase.response.get
import koncurrent.later.andThen
import koncurrent.later.await
import kotlinx.coroutines.future.await
import sentinel.bearerToken

fun Routing.installSmes(controller: SmeMonitorController) {
    get(controller.routes.smes(), controller.codec) {
        controller.auth.session(
            token = bearerToken()
        ).andThen {
            controller.smes(it).list()
        }.await()
    }

    get(controller.routes.load("{uid}"), controller.codec) {
        controller.auth.session(
            token = bearerToken()
        ).andThen {
            val uid: String by call.parameters
            controller.smes(it).load(uid)
        }.await()
    }
}