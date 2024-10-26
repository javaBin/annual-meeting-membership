package javabin.no.member_lookup.ticket.integrations.checkin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckinTicketsResponsDTO(val data: CheckinEventTicketsResponsDTO)

@Serializable
data class CheckinEventTicketsResponsDTO(val eventTickets: List<CheckinEventTicketDTO>)

@Serializable
data class CheckinEventTicketDTO(
    val category: String,
    @SerialName("event_name")
    val eventName: String,
    val crm: CrmDTO,
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
