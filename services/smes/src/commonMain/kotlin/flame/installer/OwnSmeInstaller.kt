package flame.installer

import cabinet.Attachment
import flame.OwnSmeController
import flame.SmeDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.plugins.origin
import io.ktor.server.request.header
import io.ktor.server.request.receiveMultipart
import io.ktor.server.request.receiveText
import io.ktor.server.response.respondFile
import io.ktor.server.response.respondNullable
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.util.getValue
import kase.response.get
import kase.response.patch
import kase.response.post
import koncurrent.later.andThen
import koncurrent.later.await
import koncurrent.later.then
import kotlinx.coroutines.future.await
import kotlinx.serialization.decodeFromString
import sentinel.bearerToken
import java.io.File

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

    post(controller.routes.upload("{name}"), controller.codec) {
        val scope = call.request.header(controller.resolver) ?: throw IllegalArgumentException("No scope provided")
        val supervisor = controller.supervisor(scope)
        val name: String by call.parameters
        val session = controller.auth(scope).session(bearerToken()).await()

        val coordinates = "${supervisor}/smes/${session.company.uid}/documents"
        val receiver = FileReceiver("${controller.directory}/$coordinates")

        val file = receiver.receive(name, call.receiveMultipart())
        val port = when (val p = call.request.origin.serverPort) {
            80 -> ""
            else -> ":$p"
        }
        val url = "${call.request.origin.scheme}://${call.request.origin.serverHost}$port/$coordinates/$name"
        val attachment = Attachment(uid = name, name = name, url = url, sizeInBytes = file.totalSpace.toInt())
        val service = controller.sme(session)
        service.load().andThen {
            service.update(it.copy(documents = it.documents + attachment))
        }.then {
            attachment
        }.await()
    }
}