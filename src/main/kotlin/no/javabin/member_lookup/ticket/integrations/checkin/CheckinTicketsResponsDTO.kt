package no.javabin.member_lookup.ticket.integrations.checkin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckinTicketsResponsDTO(val data: no.javabin.member_lookup.ticket.integrations.checkin.CheckinEventTicketsResponsDTO)

@Serializable
data class CheckinEventTicketsResponsDTO(val eventTickets: List<no.javabin.member_lookup.ticket.integrations.checkin.CheckinEventTicketDTO>)

@Serializable
data class CheckinEventTicketDTO(
    val category: String,
    @SerialName("event_name")
    val eventName: String,
    val crm: no.javabin.member_lookup.ticket.integrations.checkin.CrmDTO,
)

@Serializable
data class CrmDTO(
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    val email: String
) {
    val name: String
        get() = "$firstName $lastName"
}
