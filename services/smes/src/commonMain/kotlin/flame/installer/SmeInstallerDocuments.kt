@file:Suppress("Since15")

package flame.installer

import cabinet.Attachment
import flame.SmeController
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.call
import io.ktor.server.plugins.origin
import io.ktor.server.request.header
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.util.getValue
import java.io.File
import java.io.FileOutputStream
import kase.response.post
import koncurrent.later.await
import koncurrent.later.then
import sentinel.bearerToken
import sentinel.exceptions.InvalidCredentialsAuthenticationException
import sentinel.exceptions.MissingAuthenticationException

tailrec fun File.newTmpFile(attempt: Int = 0): File {
    val file = File(this, "tmp-$attempt")
    if (!file.exists()) return file.also { createNewFile() }
    return newTmpFile(attempt + 1)
}

internal fun Routing.installSmeDocuments(controller: SmeController) {
    post(controller.routes.documents(), controller.codec) {
        val scope = call.request.header(controller.resolver) ?: throw IllegalArgumentException("No scope provided")
        val session = controller.auth(scope).session(bearerToken()).await()
        val documents = File("/app/root/buckets/companies/${session.company.uid}/documents").also { it.mkdirs() }
        val tmp = documents.newTmpFile()
        val multipart = call.receiveMultipart()
        val params = mutableMapOf<String, String>()
        multipart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> params["${part.name}"] = part.value
                is PartData.FileItem -> {
                    val provider = part.streamProvider()
                    val os = FileOutputStream(tmp)
                    provider.transferTo(os)
                    os.flush()
                    os.close()
                }

                else -> throw IllegalArgumentException("Unsupported multipart form item")
            }
            part.dispose()
        }
        val name = params["name"] ?: "unknown"
        val dst = File(tmp.parent, name)
        tmp.renameTo(dst)

        val port = when (val p = call.request.origin.serverPort) {
            80 -> ""
            else -> ":$p"
        }
        val url = "${call.request.origin.scheme}://${call.request.origin.serverHost}$port${controller.routes.documents()}/${session.company.uid}/$name"
        val at = Attachment(
            uid = url,
            name = name,
            url = url,
            sizeInBytes = dst.readBytes().size,
            description = name
        )
        controller.sme(session).documents.update(at).await()
    }

    get(controller.routes.documents() + "/{company}/{name}") {
        try {
            val scope = call.request.header(controller.resolver) ?: throw IllegalArgumentException("No scope provided")
            controller.auth(scope).session(bearerToken()).await()
            val company: String by call.parameters
            val name: String by call.parameters
            val path = "/app/root/buckets/companies/${company}/documents/$name"
            val file = File(path)
            call.respondFile(file)
        } catch (err: MissingAuthenticationException) {
            call.respond(HttpStatusCode.Unauthorized)
        } catch (err: InvalidCredentialsAuthenticationException) {
            call.respond(HttpStatusCode.Unauthorized)
        } catch (err: Throwable) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}