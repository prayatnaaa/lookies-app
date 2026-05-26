package com.prayatna.lookiesapp.presentation.partner.createEvent

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.event.CreateEventParams
import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.domain.usecase.event.CreateEventUseCase
import com.prayatna.lookiesapp.presentation.partner.createEvent.state.CreateEventEffect
import com.prayatna.lookiesapp.presentation.partner.createEvent.state.CreateEventFormEvent
import com.prayatna.lookiesapp.presentation.partner.createEvent.state.CreateEventFormState
import com.prayatna.lookiesapp.presentation.partner.createEvent.state.CreateEventUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val createEventUseCase: CreateEventUseCase,
    private val eventRepository: EventRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _formState = MutableStateFlow(CreateEventFormState())
    val formState: StateFlow<CreateEventFormState> = _formState

    private val _effect = MutableSharedFlow<CreateEventEffect>()
    val effect = _effect.asSharedFlow()

    private val _uiState = MutableStateFlow(CreateEventUiState())
    val uiState: StateFlow<CreateEventUiState> = _uiState

    fun onEvent(event: CreateEventFormEvent) {
        when (event) {
            is CreateEventFormEvent.TitleChanged ->
                update { copy(title = event.value) }

            is CreateEventFormEvent.BannerChanged ->
                update { copy(bannerUri = event.uri) }

            is CreateEventFormEvent.StartDateChanged ->
                update { copy(startDate = event.value) }

            is CreateEventFormEvent.EndDateChanged ->
                update { copy(endDate = event.value) }

            is CreateEventFormEvent.LocationChanged ->
                update { copy(location = event.value) }

            is CreateEventFormEvent.LocationUrlChanged ->
                update { copy(locationUrl = event.value) }

            is CreateEventFormEvent.MaxParticipantChanged ->
                update { copy(maxParticipant = event.value) }

            is CreateEventFormEvent.MaxPaintingChanged ->
                update { copy(maxPainting = event.value) }

            is CreateEventFormEvent.MaxPaintingPerArtistChanged ->
                update { copy(maxPaintingPerArtist = event.value) }

            is CreateEventFormEvent.AboutChanged ->
                update { copy(about = event.value) }

            is CreateEventFormEvent.LoadEventMeta -> loadEventMeta()

            is CreateEventFormEvent.Submit -> submit()

            is CreateEventFormEvent.ArtistRegistrationFeeChanged ->
                update { copy(artistRegistrationFee = event.value) }

            is CreateEventFormEvent.TicketPriceChanged ->
                update { copy(ticketPrice = event.value) }

            is CreateEventFormEvent.EventFormatChanged -> {
                update { copy(eventFormat = event.value) }
            }
            is CreateEventFormEvent.EventTypeChanged -> {
                update { copy(eventType = event.value) }
            }

            is CreateEventFormEvent.PaintingArtistPercentChanged -> {
                update { copy(paintingArtistPercent = event.value) }
            }
            is CreateEventFormEvent.PaintingEventPercentChanged -> {
                update { copy(paintingEventPercent = event.value) }
            }
            is CreateEventFormEvent.PaintingPlatformPercentChanged -> {
                update { copy(paintingPlatformPercent = event.value) }
            }
            is CreateEventFormEvent.TicketArtistPercentChanged -> {
                update { copy(ticketArtistPercent = event.value) }
            }
            is CreateEventFormEvent.TicketEventPercentChanged -> {
                update { copy(ticketEventPercent = event.value) }
            }
            is CreateEventFormEvent.TicketPlatformPercentChanged -> {
                update { copy(ticketPlatformPercent = event.value) }
            }
        }
    }

    private fun update(reducer: CreateEventFormState.() -> CreateEventFormState) {
        _formState.value = _formState.value.reducer()
    }

    private fun loadEventMeta() {
        viewModelScope.launch {
            update { copy(isLoadingMeta = true, errorMessage = null) }

            when (val typesResult = eventRepository.getEventTypes()) {
                is DataResult.Success -> {
                    update { copy(eventTypes = typesResult.data, isLoadingMeta = false) }
                }
                is DataResult.Error -> {
                    update { copy(errorMessage = typesResult.error, isLoadingMeta = false) }
                    return@launch
                }
                else -> Unit
            }

            when (val formatsResult = eventRepository.getEventFormats()) {
                is DataResult.Success -> {
                    update { copy(eventFormats = formatsResult.data) }
                }
                is DataResult.Error -> {
                    update { copy(errorMessage = formatsResult.error) }
                }
                else -> Unit
            }

            update { copy(isLoadingMeta = false) }
        }
    }


    private fun submit() {
        val businessId: String = savedStateHandle["businessId"] ?: ""
        val state = _formState.value
        if (!state.isValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val params = CreateEventParams(
                title = state.title,
                bannerImageUrl = "",
                startDate = state.startDate,
                endDate = state.endDate,
                about = state.about.ifBlank { null },
                location = state.location,
                locationUrl = state.locationUrl,
                maxParticipant = state.maxParticipant?.toInt(),
                maxPainting = state.maxPainting?.toInt(),
                maxPaintingPerArtist = state.maxPaintingPerArtist?.toInt(),
                ticketPrice = state.ticketPrice?.toDouble(),
                registrationFee = state.artistRegistrationFee?.toDouble(),
                eventType = state.eventType.toInt(),
                eventFormat = state.eventFormat.toInt(),
                organizerId = businessId,
                paintingArtistPercent = state.paintingArtistPercent,
                paintingEventPercent = state.paintingEventPercent,
                paintingPlatformPercent = state.paintingPlatformPercent,
                ticketArtistPercent = state.ticketArtistPercent,
                ticketEventPercent = state.ticketEventPercent,
                ticketPlatformPercent = state.ticketPlatformPercent,

            )


            when(val result = createEventUseCase(params = params, imageUri = state.bannerUri!!)) {
                is DataResult.Error -> {
                    _uiState.update { it.copy(
                        errorMessage = result.error,
                        isSuccess = false,
                        isLoading = false
                    ) }

                    _effect.emit(CreateEventEffect.ShowError(result.error))
                }
                is DataResult.Success -> {
                    _uiState.update { it.copy(
                        isSuccess = true,
                        errorMessage = null,
                        isLoading = false
                    ) }
                    _effect.emit(CreateEventEffect.NavigateBack)
                }
                is DataResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                else -> Unit
            }
        }
    }
}
