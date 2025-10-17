package com.prayatna.lookiesapp.presentation.event.addevent

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.prayatna.lookiesapp.data.remote.response.event.AddEventResponse
import com.prayatna.lookiesapp.data.repository.EventRepository
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddEventViewModel @Inject constructor(
    private val eventRepository: EventRepository
): ViewModel() {

    private val _eventStatus = MutableStateFlow<DataResult<AddEventResponse>>(DataResult.Idle)
    val eventStatus = _eventStatus.asStateFlow()

    private val _eventState = mutableStateOf(AddEventUiState())
    val eventState get() = _eventState

    private val _detailEventState = mutableStateOf(AddDetailEventUiState())
    val detailState get() = _detailEventState

    fun setTitle(title: String) {
        _eventState.value = _eventState.value.copy(title = title)
    }

    fun setLocation(location: String) {
        _eventState.value = _eventState.value.copy(location = location)
    }

    fun setTicketPrice(price: Double) {
        _eventState.value = _eventState.value.copy(ticketPrice = price)
    }

    fun setRegistrationFee(fee: Double) {
        _eventState.value = _eventState.value.copy(registrationFee = fee)
    }

    fun setDate(date: String) {
        _eventState.value = _eventState.value.copy(date = date)
    }

    fun setLocationUrl(url: String) {
        _detailEventState.value = _detailEventState.value.copy(locationUrl = url)
    }

    fun setTicketQuantity(quantity: Int) {
        _detailEventState.value = _detailEventState.value.copy(ticketQuantity = quantity)
    }

    fun setStartTime(time: String) {
        _detailEventState.value = _detailEventState.value.copy(startTime = time)
    }

    fun setEndTime(time: String) {
        _detailEventState.value = _detailEventState.value.copy(endTime = time)
    }
}