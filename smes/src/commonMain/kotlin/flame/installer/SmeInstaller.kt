package flame.installer

import flame.SmeController
import flame.SmeKey
import flame.SmeServiceFlix
import flame.admin.SmeBusinessDto
import flame.admin.SmeContactsDto
import flame.admin.SmeDirectorDto
import flame.admin.SmeLegalComplianceDto
import flame.admin.SmeShareholderDto
import io.ktor.server.application.call
import io.ktor.server.request.receiveText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kase.response.get
import kase.response.post
import koncurrent.later.await
import kotlinx.serialization.decodeFromString
import sentinel.Sessioned
import sentinel.bearerToken

fun Routing.installSme(controller: SmeController) {
    get(controller.routes.load(), controller.codec) {
        controller.auth.session(
            token = bearerToken()
        ).andThen {
            controller.sme.load(it)
        }.await()
    }
    installSmeAdmin(controller)
    installSmeFunding(controller)
    installSmeFinance(controller)
    installSmeDocuments(controller)
}