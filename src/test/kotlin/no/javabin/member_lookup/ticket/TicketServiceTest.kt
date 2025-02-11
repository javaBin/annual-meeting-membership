package no.javabin.member_lookup.ticket

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TicketServiceTest {

    private val event = Event(
        name = "JavaZone X",
        id = 1
    )

    private val ticketAdapterFake = TicketAdapterFake().apply {
        addEvent(event)
        repeat(10) {
            addTicketToEvent(EventTicket(email = "participant$it@email.com", ticketType = TicketType.REGULAR), event.id)
        }
    }

    private val ticketService = TicketService(adapter = ticketAdapterFake)

    @Test
    fun `should filter out flexible tickets`() = runBlocking {
        ticketAdapterFake.addTicketToEvent(
            ticket = EventTicket(
                email = "flexibleticket@java.no",
                ticketType = TicketType.PARTNER,
                "JavaZone 2024 - Flexible Expo Ticket"
            ),
            eventId = event.id
        )
        assertEquals(11, ticketAdapterFake.findTickets(eventId = event.id).size)
        assertEquals(10, ticketService.tickets(eventId = event.id).size)
    }

    @Test
    fun events() = runBlocking{
        assertEquals(1, ticketService.events().size)
    }

    @Test
    fun tickets() = runBlocking {
        assertEquals(10, ticketService.tickets(ticketService.events().first().id).size)
    }
}