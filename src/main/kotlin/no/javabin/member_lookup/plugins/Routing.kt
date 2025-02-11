package no.javabin.member_lookup.plugins

import io.ktor.client.engine.cio.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import no.javabin.member_lookup.ticket.TicketService
import no.javabin.member_lookup.ticket.integrations.checkin.CheckinTicketAdapter
import no.javabin.member_lookup.ticket.integrations.checkin.CheckinTicketAdapterConfig

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

    val ticketService = TicketService(ticketAdapter)

    get {
        coroutineScope {
            val email = call.request.queryParameters["email"] ?: return@coroutineScope call.respond(
                HttpStatusCode.BadRequest,
                "We need a valid email to check for membership"
            )

            val events = ticketService.events()

            val tickets = events.map { event ->
                async {
                    ticketService.tickets(eventId = event.id)
                }
            }.awaitAll().flatten()

            val hasMembership = tickets.find { it.email == email } != null

            if (hasMembership) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}