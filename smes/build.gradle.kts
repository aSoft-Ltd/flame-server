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
                api(libs.sentinel.enterprise.authentication.service.sdk)
            }
        }

        val commonTest by getting {
            dependencies {
                api(libs.kommander.coroutines)
            }
        }
    }
}