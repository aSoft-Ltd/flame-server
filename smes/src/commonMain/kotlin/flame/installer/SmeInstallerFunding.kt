package flame.installer

import flame.SmeController
import flame.SmeKey
import flame.funding.SmeAcquisitionDto
import flame.funding.SmeBreakdownDto
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

    post(controller.routes.save(SmeKey.Funding.breakdown), controller.codec) {
        val params = controller.codec.decodeFromString<SmeBreakdownDto>(call.receiveText())
        controller.auth.session(token = bearerToken()).then {
            Sessioned(it, params)
        }.andThen {
            controller.sme.funding.saveBreakdown(it)
        }.await()
    }

    post(controller.routes.save(SmeKey.Funding.acquisition), controller.codec) {
        val params = controller.codec.decodeFromString<SmeAcquisitionDto>(call.receiveText())
        controller.auth.session(token = bearerToken()).then {
            Sessioned(it, params)
        }.andThen {
            controller.sme.funding.saveAcquisition(it)
        }.await()
    }
}