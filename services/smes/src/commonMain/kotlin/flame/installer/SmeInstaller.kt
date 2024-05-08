package flame.installer

import flame.OwnSmeController
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.util.getValue
import koncurrent.later.await
import sentinel.bearerToken
import sentinel.exceptions.MissingAuthenticationException
import java.io.File

fun Routing.installSme(controller: OwnSmeController) {
    get("{supervisor}/smes/{company}/documents/{name}") {
        try {
            val token = bearerToken()
            val name: String by call.parameters
            val company: String by call.parameters
            val path = when (val scope = call.request.header(controller.resolver)) {
                null -> {
                    val supervisor: String by call.parameters
                    "${controller.directory}/$supervisor/smes/$company/documents/$name"
                }

                else -> {
                    val supervisor = controller.supervisor(scope)
                    val session = controller.auth(scope).session(token).await()
                    if (session.company.uid != company) throw IllegalArgumentException("One sme can not access another sme's documents")
                    "${controller.directory}/$supervisor/smes/$company/documents/$name"
                }
            }
            val file = File(path)
            if(file.exists()) {
                call.respondFile(file)
            } else {
                call.respond(HttpStatusCode.NotFound, "File not found")
            }
        } catch (err: MissingAuthenticationException) {
            call.respond(HttpStatusCode.Unauthorized, err.message)
        } catch (err: Throwable) {
            call.respond(HttpStatusCode.InternalServerError, err.message ?: "Internal Server Error")
        }
    }
}