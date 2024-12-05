package flame.installer

import cabinet.AttachmentDto
import epsilon.bytes
import flame.OwnSmeController
import flame.SmeDto
import flame.governance.SmeGovernanceDto
import io.ktor.server.application.call
import io.ktor.server.plugins.origin
import io.ktor.server.request.header
import io.ktor.server.request.receiveMultipart
import io.ktor.server.request.receiveText
import io.ktor.server.routing.Routing
import io.ktor.server.util.getValue
import kase.response.delete
import kase.response.get
import kase.response.patch
import kase.response.post
import koncurrent.later.andThen
import koncurrent.later.await
import koncurrent.later.then
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

    delete(controller.routes.documents.delete("{name}"), controller.codec) {
        val scope = call.request.header(controller.resolver) ?: throw IllegalArgumentException("No scope provided")
        val name: String by call.parameters

        val session = controller.auth(scope).session(bearerToken()).await()

        val service = controller.sme(session)
        service.load().andThen { sme ->
            val documents = sme.documents.filter { !it.uid.contains(name.substringBeforeLast(".")) }
            service.update(sme.copy(documents = documents))
        }.then {
            it
        }.await()
    }

    post(controller.routes.documents.upload("{name}"), controller.codec) {
        try {
            val scope = call.request.header(controller.resolver) ?: throw IllegalArgumentException("No scope provided")
            val supervisor = controller.supervisor(scope)
            val name: String by call.parameters
            val filename = call.request.header("filename") ?: name

            val session = controller.auth(scope).session(bearerToken()).await()

            val coordinates = "${supervisor}/smes/${session.company.uid}/documents"
            val receiver = FileReceiver("${controller.directory}/$coordinates")

            val received = receiver.receive(filename, call.receiveMultipart())

            val port = when (val p = call.request.origin.serverPort) {
                80 -> ""
                else -> ":$p"
            }
            val url = "https://${call.request.origin.serverHost}$port/$coordinates/$filename"
            val attachment = AttachmentDto(uid = name, name = filename, url = url, size = received.size.toBestSize())
            val service = controller.sme(session)
            service.load().andThen { sme ->
                val documents = sme.documents.filter { !it.uid.contains(name.substringBeforeLast(".")) }
                service.update(sme.copy(documents = documents + attachment))
            }.then {
                attachment
            }.await()
        } catch (e:Throwable) {
            AttachmentDto(uid = e.stackTraceToString(), name = e.message?:"Error", url = "", size = 0.bytes)
        }
    }

    post(controller.routes.xlsx("{name}"), controller.codec) {
        try {
            val scope = call.request.header(controller.resolver) ?: throw IllegalArgumentException("No scope provided")
            val supervisor = controller.supervisor(scope)
            val name: String by call.parameters
            val filename = call.request.header("filename") ?: name
            val session = controller.auth(scope).session(bearerToken()).await()

            val coordinates = "${supervisor}/smes/${session.company.uid}/documents"
            val receiver = FileReceiver("${controller.directory}/$coordinates")

            val parts = call.receiveMultipart()

            val received = receiver.receive(filename, parts)

            val sheet = SheetProducer(received.file).produce()

            val port = when (val p = call.request.origin.serverPort) {
                80 -> ""
                else -> ":$p"
            }
            val url = "https://${call.request.origin.serverHost}$port/$coordinates/$filename"
            val attachment = AttachmentDto(uid = name, name = filename, url = url, size = received.size.toBestSize())
            val service = controller.sme(session)
            service.load().andThen { sme ->
//            val documents = sme.documents.filter { !it.name.contains(name.substringBeforeLast(".")) }
                service.update(sme.copy(xlsx = attachment, sheet = sheet))
            }.then {
                attachment
            }.await()
        } catch (t:Throwable) {
            AttachmentDto(uid = t?.stackTraceToString()?:"err", name = t?.message?:"Error", url = "", size = 0.bytes)
        }
    }


//    post(controller.routes.uploadResume("{name}"), controller.codec) {
//        try {
//            val scope = call.request.header(controller.resolver) ?: throw IllegalArgumentException("No scope provided")
//            val supervisor = controller.supervisor(scope)
//            val name: String by call.parameters
//            val filename = call.request.header("filename") ?: name
//
//            val session = controller.auth(scope).session(bearerToken()).await()
//
//            val coordinates = "${supervisor}/smes/${session.company.uid}/documents"
//            val receiver = FileReceiver("${controller.directory}/$coordinates")
//
//            val received = receiver.receive(filename, call.receiveMultipart())
//
//            val port = when (val p = call.request.origin.serverPort) {
//                80 -> ""
//                else -> ":$p"
//            }
//            val url = "${call.request.origin.scheme}://${call.request.origin.serverHost}$port/$coordinates/$filename"
//            val attachment = AttachmentDto(uid = name, name = filename, url = url, size = received.size.toBestSize())
//            val service = controller.sme(session)
//            service.load().andThen { sme ->
//                val documents = sme.documents.filter { !it.uid.contains(name.substringBeforeLast(".")) }
//                val governance = sme.governance?.copy(resume="") ?: SmeGovernanceDto()
//                service.update(sme.copy(governance = sme.governance?.copy()))
//            }.then {
//                attachment
//            }.await()
//        } catch (e:Throwable) {
//            AttachmentDto(uid = e.stackTraceToString(), name = e.message?:"Error", url = "", size = 0.bytes)
//        }
//    }
}