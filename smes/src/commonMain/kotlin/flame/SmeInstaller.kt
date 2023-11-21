package flame

import io.ktor.server.application.call
import io.ktor.server.request.receiveText
import io.ktor.server.routing.Routing
import kase.response.get
import kase.response.post
import kotlinx.serialization.decodeFromString
import sentinel.Sessioned
import sentinel.bearerToken

fun Routing.installSme(controller: SmeController) {
    get(controller.routes.load(), controller.codec) {
        controller.auth.session(
            token = bearerToken()
        ).andThen {
            controller.sme.load(it)
        }
    }

    post(controller.routes.saveContacts(), controller.codec) {
        val params = controller.codec.decodeFromString<SmeContactsDto>(call.receiveText())
        controller.auth.session(
            token = bearerToken()
        ).then {
            Sessioned(it, params)
        }.andThen {
            controller.sme.admin.saveContacts(it)
        }
    }
}