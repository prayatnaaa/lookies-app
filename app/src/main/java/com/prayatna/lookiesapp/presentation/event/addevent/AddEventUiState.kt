package com.prayatna.lookiesapp.presentation.event.addevent
import com.prayatna.lookiesapp.data.model.DetailEvent
import com.prayatna.lookiesapp.data.model.Event

data class AddEventUiState(
    val title: String = "",
    val location: String = "",
    val ticketPrice: Double = 0.0,
    val registrationFee: Double = 0.0,
    val date: String = "",
    val bannerImageUrl: String? = null,
    val organizerId: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class AddDetailEventUiState(
    val locationUrl: String = "",
    val ticketQuantity: Int = 0,
    val startTime: String = "",
    val endTime: String = ""
)

fun AddEventUiState.toEvent(): Event {
    return Event(
        id = null,
        organizerId = organizerId,
        title = title,
        bannerImageUrl = bannerImageUrl,
        location = location,
        ticketPrice = ticketPrice,
        registrationFee = registrationFee,
        date = date,
        status = null
    )
}

fun AddDetailEventUiState.toDetailEvent(): DetailEvent {
    return DetailEvent(
        locationUrl = locationUrl,
        ticketQuantity = ticketQuantity,
        startTime = startTime,
        endTime = endTime
    )
}
