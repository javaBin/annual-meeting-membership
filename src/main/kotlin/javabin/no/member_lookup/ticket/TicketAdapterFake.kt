package javabin.no.member_lookup.ticket

class TicketAdapterFake : TicketAdapter {
    private val events: MutableList<Event> = mutableListOf()
    private val eventTickets: MutableMap<Long, MutableList<EventTicket>> = mutableMapOf()

    override suspend fun findEvents() = events.toList()

    override suspend fun findTickets(eventId: Long) = eventTickets[eventId] ?: emptyList()

    fun addTicketToEvent(ticket: EventTicket, eventId: Long): Boolean {
        if (events.find { it.id == eventId } == null) {
            events.add(
                Event(
                    name = "Event",
                    id = eventId
                )
            )
        }
        return eventTickets.computeIfAbsent(eventId) { mutableListOf() }.add(ticket)
    }

    fun addEvent(event: Event) {
        events += event
    }
}