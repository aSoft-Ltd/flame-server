package flame.installer

import flame.SmeController
import flame.SmeKey
import flame.admin.SmeBusinessDto
import flame.admin.SmeContactsDto
import flame.admin.SmeDirectorDto
import flame.admin.SmeLegalComplianceDto
import flame.admin.SmeShareholderDto
import io.ktor.server.application.call
import io.ktor.server.request.receiveText
import io.ktor.server.routing.Routing
import kase.response.post
import koncurrent.later.andThen
import koncurrent.later.await
import koncurrent.later.then
import kotlinx.serialization.decodeFromString
import sentinel.bearerToken

internal fun Routing.installSmeAdmin(controller: SmeController) {
    post(controller.routes.save(SmeKey.Admin.contacts), controller.codec) {
        val params = controller.codec.decodeFromString<SmeContactsDto>(call.receiveText())
        controller.auth.session(token = bearerToken()).then {
            controller.sme(it)
        }.andThen { service ->
            service.admin.update(params)
        }.await()
    }

    post(controller.routes.save(SmeKey.Admin.businesses), controller.codec) {
        val params = controller.codec.decodeFromString<SmeBusinessDto>(call.receiveText())
        controller.auth.session(token = bearerToken()).then {
            controller.sme(it)
        }.andThen { service ->
            service.admin.update(params)
        }.await()
    }

    post(controller.routes.save(SmeKey.Admin.legal), controller.codec) {
        val params = controller.codec.decodeFromString<SmeLegalComplianceDto>(call.receiveText())
        controller.auth.session(token = bearerToken()).then {
            controller.sme(it)
        }.andThen { session ->
            session.admin.update(params)
        }.await()
    }

    post(controller.routes.save(SmeKey.Admin.shareholders), controller.codec) {
        val params = controller.codec.decodeFromString<List<SmeShareholderDto>>(call.receiveText())
        controller.auth.session(token = bearerToken()).then {
            controller.sme(it)
        }.andThen { session ->
            session.admin.updateShareholders(params)
        }.await()
    }

    post(controller.routes.save(SmeKey.Admin.directors), controller.codec) {
        val params = controller.codec.decodeFromString<List<SmeDirectorDto>>(call.receiveText())
        controller.auth.session(token = bearerToken()).then {
            controller.sme(it)
        }.andThen { session ->
            session.admin.updateDirectors(params)
        }.await()
    }
}