package javabin.no.member_lookup.ticket

interface TicketAdapter {
    suspend fun findEvents(): List<Event>
    suspend fun findTickets(eventId: Long): List<EventTicket>
}

data class EventTicket(val email: String, val ticketType: TicketType, val category: String = "")

enum class TicketType {
    REGULAR, PARTNER
}

data class Event(val name: String, val id: Long)
