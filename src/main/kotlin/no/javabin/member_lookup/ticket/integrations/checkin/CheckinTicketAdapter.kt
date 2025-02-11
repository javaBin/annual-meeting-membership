package no.javabin.member_lookup.ticket.integrations.checkin

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import no.javabin.member_lookup.ticket.Event
import no.javabin.member_lookup.ticket.EventTicket
import no.javabin.member_lookup.ticket.TicketAdapter
import no.javabin.member_lookup.ticket.TicketType

data class CheckinTicketAdapterConfig(
    val clientId: String,
    val clientSecret: String,
    val customerId: String,
)

class CheckinTicketAdapter(private val adapterConfig: CheckinTicketAdapterConfig, httpEngine: HttpClientEngine) :
    TicketAdapter {
    private val ticketsGraphqlDocument = javaClass.getResource("/checkin/checkin-tickets.graphql")?.readText()
    private val eventsGraphqlDocument = javaClass.getResource("/checkin/checkin-events.graphql")?.readText()

    private val url =
        with(adapterConfig) { "https://api.checkin.no/graphql?client_id=$clientId&client_secret=$clientSecret" }

    private val httpClient = HttpClient(httpEngine) {
        expectSuccess = true

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }

        defaultRequest {
            this.url(this@CheckinTicketAdapter.url)
        }
    }

    override suspend fun findEvents(): List<Event> {
        val requestBody = """
                {
                    "query": "${eventsGraphqlDocument?.replace("\n", "\\n")?.replace("\"", "\\\"")}",
                    "operationName": "events",
                     "variables": {
                        "customerId": ${adapterConfig.customerId},
                        "nameStartsWith": "JavaZone 2024"
                     }
                }
            """.trimIndent()

        val response = httpClient.post {
            contentType(ContentType.Application.Json)
            setBody(
                requestBody
            )
        }
        val data: CheckinEventsResponseDTO = response.body()
        return data.data.events.data.map { Event(name = it.name, id = it.id) }
    }

    override suspend fun findTickets(eventId: Long): List<EventTicket> {
        val response = httpClient.post {
            contentType(ContentType.Application.Json)
            setBody(
                """
            {
                "query": "${ticketsGraphqlDocument?.replace("\n", "\\n")?.replace("\"", "\\\"")}",
                "operationName": "tickets",
                "variables": {
                    "customerId": ${adapterConfig.customerId},
                    "eventId": $eventId
                }
            }
            """.trimIndent()
            )
        }
        val data: no.javabin.member_lookup.ticket.integrations.checkin.CheckinTicketsResponsDTO = response.body()
        return data.data.eventTickets.map {
            EventTicket(
                email = it.crm.email,
                ticketType = if (it.eventName.contains("Partner")) TicketType.PARTNER else TicketType.REGULAR,
                category = it.category,
            )
        }
    }
}