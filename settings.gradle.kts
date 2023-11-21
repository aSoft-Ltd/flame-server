import java.io.File

pluginManagement {
    includeBuild("../build-logic")
}

plugins {
    id("multimodule")
}

fun includeSubs(base: String, path: String = base, vararg subs: String) {
    subs.forEach {
        include(":$base-$it")
        project(":$base-$it").projectDir = File("$path/$it")
    }
}

listOf(
    "cinematic", "keep", "lexi", "captain", "neat",
    "kash-api", "geo-api", "geo-client",
    "kronecker", "epsilon-api", "krono-core", "hormone", "identifier-api",
    "kommerce", "kollections", "koncurrent", "kommander", "cabinet-api", "flame-core"
).forEach { includeBuild("../$it") }

rootProject.name = "flame-server"

includeSubs("flame-sdk", ".", "smes")