package com.prayatna.lookiesapp.presentation.event.addevent

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.data.remote.mapper.toDto
import com.prayatna.lookiesapp.data.remote.response.event.AddEventResponse
import com.prayatna.lookiesapp.data.repository.EventRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.compressImage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    @ApplicationContext private val context: Context
): ViewModel() {

    private val _imageUri = mutableStateOf<Uri?>(null)
    val imageUri get() = _imageUri

    private val _eventStatus = MutableStateFlow<DataResult<AddEventResponse>>(DataResult.Idle)
    val eventStatus = _eventStatus.asStateFlow()

    private val _eventState = mutableStateOf(AddEventUiState())
    val eventState get() = _eventState

    private val _detailEventState = mutableStateOf(AddDetailEventUiState())
    val detailState get() = _detailEventState

    fun setImageUri(uri: Uri) {
        _imageUri.value = uri
    }

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

    fun addEvent() {
        _eventStatus.value = DataResult.Loading
        viewModelScope.launch {
            val compressImage = _imageUri.value?.compressImage(context = context, compressionThreshold = 1_000_000L)
            if (compressImage == null || compressImage.isEmpty()) {
                _eventStatus.value = DataResult.Error("Please select an image")
                return@launch
            }

            val result = eventRepository.addEvent(
                event = eventState.value.toEvent().toDto(),
                detailEvent = detailState.value.toDetailEvent().toDto(),
                imageByte = compressImage
            )
            _eventStatus.value = result
        }
    }
}