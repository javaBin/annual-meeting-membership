package javabin.no.member_lookup.ticket.integrations.checkin

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import javabin.no.member_lookup.ticket.Event
import javabin.no.member_lookup.ticket.EventTicket
import javabin.no.member_lookup.ticket.TicketAdapter
import kotlinx.serialization.json.Json

class CheckinTicketAdapter(clientId: String, clientSecret: String, httpEngine: HttpClientEngine) : TicketAdapter {
    private val url = "https://api.checkin.no/graphql?client_id=$clientId&client_secret=$clientSecret"

    private val httpClient = HttpClient(httpEngine) {
        expectSuccess = true

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(Logging)
    }

    override suspend fun findEvents(): List<Event> {
        TODO("Not yet implemented")
    }

    override suspend fun findTickets(eventId: Long): List<EventTicket> {
        TODO("Not yet implemented")
    }
}