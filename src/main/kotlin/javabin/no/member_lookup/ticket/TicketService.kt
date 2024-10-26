package javabin.no.member_lookup.ticket

class TicketService(private val adapter: TicketAdapter) {
    fun events() = adapter.findEvents()
    fun tickets(eventId: Long): List<EventTicket> {
        val tickets = adapter.findTickets(eventId = eventId)
        return tickets.filterNot { it.category.contains("Flexible") }
    }
}