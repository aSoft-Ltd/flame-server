package flame.installer

import flame.MonSmeController
import flame.SmeDto
import flame.admin.SmeBusinessDto
import io.ktor.server.application.call
import io.ktor.server.request.receiveText
import io.ktor.server.routing.Routing
import io.ktor.server.util.getValue
import kase.response.get
import kase.response.patch
import kase.response.post
import koncurrent.later.andThen
import koncurrent.later.await
import kotlinx.serialization.decodeFromString
import sentinel.bearerToken

fun Routing.installMonSme(controller: MonSmeController) {
    get(controller.routes.load(),controller.codec) {
        controller.auth.session(token = bearerToken()).andThen {
            controller.sme(it).list()
        }.await()
    }

    get(controller.routes.load("{uid}"), controller.codec) {
        val uid : String by call.parameters
        controller.auth.session(token = bearerToken()).andThen {
            controller.sme(it).load(uid)
        }.await()
    }

    post(controller.routes.create(),controller.codec) {
        val params = controller.codec.decodeFromString<SmeBusinessDto>(call.receiveText())
        controller.auth.session(token = bearerToken()).andThen {
            controller.sme(it).create(params)
        }.await()
    }

    patch(controller.routes.update(),controller.codec) {
        val params = controller.codec.decodeFromString<SmeDto>(call.receiveText())
        controller.auth.session(token = bearerToken()).andThen {
            controller.sme(it).update(params)
        }.await()
    }
}