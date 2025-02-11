package no.javabin.member_lookup.ticket.integrations.checkin

import kotlinx.serialization.Serializable

@Serializable
data class CheckinEventsResponseDTO(val data: EventDataResponsDTO)

@Serializable
data class EventDataResponsDTO(val events: EventDataDTO)

@Serializable
class EventDataDTO(val data: List<EventDTO>)

@Serializable
class EventDTO(val id: Long, val name: String)
