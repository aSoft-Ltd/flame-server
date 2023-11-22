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
import koncurrent.later.await
import kotlinx.serialization.decodeFromString
import sentinel.Sessioned
import sentinel.bearerToken

fun Routing.installSmeAdmin(controller: SmeController) {
    post(controller.routes.save(SmeKey.Admin.contacts), controller.codec) {
        val params = controller.codec.decodeFromString<SmeContactsDto>(call.receiveText())
        controller.auth.session(token = bearerToken()).then {
            Sessioned(it, params)
        }.andThen {
            controller.sme.admin.saveContacts(it)
        }.await()
    }

    post(controller.routes.save(SmeKey.Admin.businesses), controller.codec) {
        val params = controller.codec.decodeFromString<SmeBusinessDto>(call.receiveText())
        controller.auth.session(token = bearerToken()).then {
            Sessioned(it, params)
        }.andThen {
            controller.sme.admin.saveBusiness(it)
        }.await()
    }

    post(controller.routes.save(SmeKey.Admin.legal), controller.codec) {
        val params = controller.codec.decodeFromString<SmeLegalComplianceDto>(call.receiveText())
        controller.auth.session(token = bearerToken()).then {
            Sessioned(it, params)
        }.andThen {
            controller.sme.admin.saveLegal(it)
        }.await()
    }

    post(controller.routes.save(SmeKey.Admin.shareholders), controller.codec) {
        val params = controller.codec.decodeFromString<List<SmeShareholderDto>>(call.receiveText())
        controller.auth.session(token = bearerToken()).then {
            Sessioned(it, params)
        }.andThen {
            controller.sme.admin.saveShareholders(it)
        }.await()
    }

    post(controller.routes.save(SmeKey.Admin.directors), controller.codec) {
        val params = controller.codec.decodeFromString<List<SmeDirectorDto>>(call.receiveText())
        controller.auth.session(token = bearerToken()).then {
            Sessioned(it, params)
        }.andThen {
            controller.sme.admin.saveDirectors(it)
        }.await()
    }
}