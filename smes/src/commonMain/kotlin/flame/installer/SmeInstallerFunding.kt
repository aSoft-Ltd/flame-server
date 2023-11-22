package flame.installer

import flame.SmeController
import flame.SmeKey
import flame.admin.SmeBusinessDto
import flame.admin.SmeContactsDto
import flame.admin.SmeDirectorDto
import flame.admin.SmeLegalComplianceDto
import flame.admin.SmeShareholderDto
import flame.funding.SmeInvestmentDto
import io.ktor.server.application.call
import io.ktor.server.request.receiveText
import io.ktor.server.routing.Routing
import kase.response.post
import koncurrent.later.await
import kotlinx.serialization.decodeFromString
import sentinel.Sessioned
import sentinel.bearerToken

fun Routing.installSmeFunding(controller: SmeController) {
    post(controller.routes.save(SmeKey.Funding.investment), controller.codec) {
        val params = controller.codec.decodeFromString<SmeInvestmentDto>(call.receiveText())
        controller.auth.session(token = bearerToken()).then {
            Sessioned(it, params)
        }.andThen {
            controller.sme.funding.saveInvestment(it)
        }.await()
    }
}