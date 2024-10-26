package javabin.no.member_lookup.integrations.checkin

class CheckinService(private val adapter: CheckinAdapter) {
    fun events() = adapter.findEvents()
    fun tickets(eventId: Long): List<EventTicket> {
        val tickets = adapter.findTickets(eventId = eventId)
        return tickets.filterNot { it.category.contains("Flexible") }
    }
}