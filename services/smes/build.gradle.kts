plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

kotlin {
    jvm { library() }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.flame.schemes.smes)
                api(libs.geo.coordinates)
                api(libs.krono.api)
                api(db.mongo)
                api(libs.kase.response.ktor.server)
                api(ktor.server.core)
                api(libs.kronecker.core)
                api(libs.cabinet.api.core)
                api(libs.identifier.legal.dtos)
                api(libs.sentinel.services.enterprise.authentication.email.core)?.because(
                    "We need to access UserSession to make sure that only authenticated individuals have access to the sme service"
                )
                api(libs.sentinel.controllers.enterprise.authentication.email)?.because(
                    "We need to access bearer token to make sure that only authenticated individuals have access to the sme service"
                )
            }
        }

        val commonTest by getting {
            dependencies {
                api(libs.kommander.coroutines)
            }
        }
    }
}