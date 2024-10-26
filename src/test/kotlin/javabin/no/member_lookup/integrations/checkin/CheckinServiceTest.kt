package javabin.no.member_lookup.integrations.checkin

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class CheckinServiceTest {

    private val event = Event(
        name = "JavaZone X",
        id = 1
    )

    private val checkinAdapterFake = CheckinAdapterFake().apply {
        addEvent(event)
        repeat(10) {
            addTicketToEvent(EventTicket(email = "participant$it@email.com", ticketType = TicketType.REGULAR), event.id)
        }
    }

    private val checkinService = CheckinService(adapter = checkinAdapterFake)

    @Test
    fun `should filter out flexible tickets`() {
        checkinAdapterFake.addTicketToEvent(
            ticket = EventTicket(
                email = "flexibleticket@java.no",
                ticketType = TicketType.PARTNER,
                "JavaZone 2024 - Flexible Expo Ticket"
            ),
            eventId = event.id
        )
        assertEquals(11, checkinAdapterFake.findTickets(eventId = event.id).size)
        assertEquals(10, checkinService.tickets(eventId = event.id).size)
    }

    @Test
    fun events() {
        assertEquals(1, checkinService.events().size)
    }

    @Test
    fun tickets() {
        assertEquals(10, checkinService.tickets(checkinService.events().first().id).size)
    }
}