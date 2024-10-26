package javabin.no.member_lookup.ticket.integrations.checkin

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import javabin.no.member_lookup.ticket.Event
import javabin.no.member_lookup.ticket.EventTicket
import javabin.no.member_lookup.ticket.TicketAdapter

class CheckinTicketAdapter : TicketAdapter {
    private val httpClient = HttpClient(CIO) {
        expectSuccess = true

        install(Logging)
    }

    override fun findEvents(): List<Event> {
        TODO("Not yet implemented")
    }

    override fun findTickets(eventId: Long): List<EventTicket> {
        TODO("Not yet implemented")
    }
}