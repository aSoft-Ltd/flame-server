package flame.installer

import flame.SmeController
import flame.SmeKey
import flame.finance.SmeBackOfficeDto
import flame.finance.SmeFinancialAcquisitionDto
import flame.finance.SmeFinancialStatusDto
import io.ktor.server.application.call
import io.ktor.server.request.receiveText
import io.ktor.server.routing.Routing
import kase.response.post
import koncurrent.later.await
import koncurrent.later.then
import koncurrent.later.andThen
import kotlinx.serialization.decodeFromString
import sentinel.Sessioned
import sentinel.bearerToken

internal fun Routing.installSmeFinance(controller: SmeController) {
    post(controller.routes.save(SmeKey.Finance.office), controller.codec) {
        val params = controller.codec.decodeFromString<SmeBackOfficeDto>(call.receiveText())
        controller.auth.session(token = bearerToken()).then {
            controller.sme(it)
        }.andThen { service ->
            service.finance.update(params)
        }.await()
    }

    post(controller.routes.save(SmeKey.Finance.status), controller.codec) {
        val params = controller.codec.decodeFromString<SmeFinancialStatusDto>(call.receiveText())
        controller.auth.session(token = bearerToken()).then {
            controller.sme(it)
        }.andThen { service ->
            service.finance.update(params)
        }.await()
    }

    post(controller.routes.save(SmeKey.Finance.acquisition), controller.codec) {
        val params = controller.codec.decodeFromString<SmeFinancialAcquisitionDto>(call.receiveText())
        controller.auth.session(token = bearerToken()).then {
            controller.sme(it)
        }.andThen { service ->
            service.finance.update(params)
        }.await()
    }
}