package javabin.no.member_lookup.integrations.checkin

interface CheckinAdapter {
    fun findEvents(): List<Event>
    fun findTickets(eventId: Long): List<EventTicket>
}

data class EventTicket(val email: String, val ticketType: TicketType, val category: String = "")

enum class TicketType {
    REGULAR, PARTNER
}

data class Event(val name: String, val id: Long)
