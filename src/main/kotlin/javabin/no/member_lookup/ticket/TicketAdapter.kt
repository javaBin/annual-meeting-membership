package javabin.no.member_lookup.ticket

import kotlinx.serialization.Serializable

interface TicketAdapter {
    suspend fun findEvents(): List<Event>
    suspend fun findTickets(eventId: Long): List<EventTicket>
}

@Serializable
data class EventTicket(val email: String, val ticketType: TicketType, val category: String = "")

enum class TicketType {
    REGULAR, PARTNER
}

data class Event(val name: String, val id: Long)
