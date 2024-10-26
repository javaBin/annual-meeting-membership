package javabin.no.member_lookup

import io.ktor.server.application.*
import io.ktor.server.netty.*
import javabin.no.member_lookup.plugins.configureRouting


fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    configureRouting()
}
