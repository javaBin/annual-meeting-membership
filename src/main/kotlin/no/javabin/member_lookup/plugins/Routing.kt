package no.javabin.member_lookup.plugins

import io.ktor.client.engine.cio.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.*
import no.javabin.member_lookup.ticket.TicketService
import no.javabin.member_lookup.ticket.integrations.checkin.CheckinTicketAdapter
import no.javabin.member_lookup.ticket.integrations.checkin.CheckinTicketAdapterConfig
import kotlin.time.Duration.Companion.seconds

fun Application.configureRouting() {
    routing {
        isMemberRoute()
    }
}

fun Routing.isMemberRoute() = route("membership") {
    val adapterConfig = with(environment.config) {
        CheckinTicketAdapterConfig(
            clientId = property("checkin.client_id").getString(),
            clientSecret = property("checkin.client_secret").getString(),
            customerId = property("checkin.customer_id").getString()
        )
    }
    val ticketAdapter = CheckinTicketAdapter(
        adapterConfig = adapterConfig,
        httpEngine = CIO.create(),
    )

    val javabinHeroesEmails =
        environment.config.propertyOrNull("javabin.heroes")?.getString()?.split("\n") ?: emptyList()
    application.log.info("Read ${javabinHeroesEmails.size} javaBin heroes into memory")

    val ticketService = TicketService(ticketAdapter)

    val allEmails = mutableListOf<String>()

    runBlocking {
        withTimeout(15.seconds) {
            val events = ticketService.events()

            application.log.info("Found ${events.size} events, reading ticket emails for each event")

            val ticketParticipantEmails = events.map { event ->
                async {
                    ticketService.tickets(eventId = event.id)
                }
            }.awaitAll().flatten().map { ticket -> ticket.email }

            allEmails.addAll(ticketParticipantEmails + javabinHeroesEmails)
        }
    }

    get {
        coroutineScope {
            val membershipEmails = allEmails.toSet()

            val email = call.request.queryParameters["email"] ?: return@coroutineScope call.respond(
                HttpStatusCode.BadRequest,
                "We need a valid email to check for membership"
            )

            val hasMembership = membershipEmails.find { it == email } != null

            if (hasMembership) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}