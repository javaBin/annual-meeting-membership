package no.javabin.member_lookup

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import no.javabin.member_lookup.plugins.configureRouting


fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    install(CORS) {
        allowHost("java.no")
    }
    configureRouting()
}
