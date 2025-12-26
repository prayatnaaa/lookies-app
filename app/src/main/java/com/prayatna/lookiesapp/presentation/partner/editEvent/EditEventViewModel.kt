package com.prayatna.lookiesapp.presentation.partner.editEvent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.mapper.toEditEventInput
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.domain.usecase.event.EditEventUseCase
import com.prayatna.lookiesapp.presentation.partner.editEvent.state.EditEventFormEvent
import com.prayatna.lookiesapp.presentation.partner.editEvent.state.EditEventFormState
import com.prayatna.lookiesapp.presentation.partner.editEvent.state.EditEventUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditEventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val editEventUseCase: EditEventUseCase
) : ViewModel() {

    private val _formState = MutableStateFlow(EditEventFormState())
    val formState: StateFlow<EditEventFormState> = _formState

    private val _uiState = MutableStateFlow(EditEventUiState())
    val uiState: StateFlow<EditEventUiState> = _uiState

    private var eventId: String? = null

    fun onEvent(event: EditEventFormEvent) {
        when (event) {
            is EditEventFormEvent.TitleChanged ->
                update { copy(title = event.value) }

            is EditEventFormEvent.StartDateChanged ->
                update { copy(startDate = event.value) }

            is EditEventFormEvent.EndDateChanged ->
                update { copy(endDate = event.value) }

            is EditEventFormEvent.LocationChanged ->
                update { copy(location = event.value) }

            is EditEventFormEvent.LocationUrlChanged ->
                update { copy(locationUrl = event.value) }

            is EditEventFormEvent.MaxParticipantChanged ->
                update { copy(maxParticipant = event.value) }

            is EditEventFormEvent.MaxPaintingChanged ->
                update { copy(maxPainting = event.value) }

            is EditEventFormEvent.MaxPaintingPerArtistChanged ->
                update { copy(maxPaintingPerArtist = event.value) }

            is EditEventFormEvent.AboutChanged ->
                update { copy(about = event.value) }

            is EditEventFormEvent.TicketPriceChanged ->
                update { copy(ticketPrice = event.value) }

            is EditEventFormEvent.ArtistRegistrationFeeChanged ->
                update { copy(artistRegistrationFee = event.value) }

            is EditEventFormEvent.EventTypeChanged ->
                update { copy(eventType = event.value) }

            is EditEventFormEvent.EventFormatChanged ->
                update { copy(eventFormat = event.value) }

            EditEventFormEvent.LoadEventMeta -> loadMeta()

            EditEventFormEvent.Submit -> submit()
        }
    }

    fun loadEvent(eventId: String) = viewModelScope.launch {
        this@EditEventViewModel.eventId = eventId
        _uiState.update { it.copy(isLoading = true) }

        when (val result = eventRepository.getEvent(eventId)) {
            is DataResult.Success -> {
                prefill(result.data)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        data = result.data,
                    )
                }
            }
            is DataResult.Error -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.error
                    )
                }
            }
            else -> Unit
        }
    }


    private fun update(block: EditEventFormState.() -> EditEventFormState) {
        _formState.update(block)
    }

    private fun loadMeta() = viewModelScope.launch {
        _formState.update { it.copy(isLoadingMeta = true) }

        val types = eventRepository.getEventTypes()
        val formats = eventRepository.getEventFormats()

        if (types is DataResult.Success && formats is DataResult.Success) {
            _formState.update {
                it.copy(
                    eventTypes = types.data,
                    eventFormats = formats.data,
                    isLoadingMeta = false
                )
            }
        } else {
            _formState.update {
                it.copy(
                    errorMessage = "Failed load event meta",
                    isLoadingMeta = false
                )
            }
        }
    }

    private fun prefill(event: Event) {
        _formState.update {
            it.copy(
                bannerImage = event.bannerImageUrl,
                title = event.title,
                startDate = event.startDate,
                endDate = event.endDate,
                location = event.location,
                locationUrl = event.locationUrl,
                maxParticipant = event.maxParticipant?.toString(),
                maxPainting = event.maxPainting?.toString(),
                maxPaintingPerArtist = event.maxPaintingPerArtist?.toString(),
                ticketPrice = event.ticketPrice?.toString(),
                artistRegistrationFee = event.artistRegistrationFee?.toString(),
                about = event.about.orEmpty(),
                eventType = event.eventType,
                eventFormat = event.eventFormat
            )
        }
    }

    private fun submit() = viewModelScope.launch {
        val id = eventId ?: return@launch

        _uiState.update { it.copy(isLoading = true) }

        val input = formState.value.toEditEventInput()

        when (val result = editEventUseCase(id, input)) {
            is DataResult.Success -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
            }
            is DataResult.Error -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.error
                    )
                }
            }

            else -> {}
        }
    }

}
