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
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val effect: SharedFlow<CreateEventEffect> = _effect.asSharedFlow()

    private val _uiState = MutableStateFlow(CreateEventUiState())
    val uiState: StateFlow<CreateEventUiState> = _uiState.asStateFlow()

    fun onEvent(event: CreateEventFormEvent) {
        when (event) {
            is CreateEventFormEvent.TitleChanged -> update { copy(title = event.value) }
            is CreateEventFormEvent.BannerChanged -> update { copy(bannerUri = event.uri) }
            is CreateEventFormEvent.StartDateChanged -> update { copy(startDate = event.value) }
            is CreateEventFormEvent.EndDateChanged -> update { copy(endDate = event.value) }
            is CreateEventFormEvent.PaintingSubmissionDeadlineChanged -> update { copy(paintingSubmissionDeadline = event.value) }
            is CreateEventFormEvent.RegistrationStartDateChanged -> update { copy(registrationStartDate = event.value) }
            is CreateEventFormEvent.RegistrationEndDateChanged -> update { copy(registrationEndDate = event.value) }
            is CreateEventFormEvent.LocationChanged -> update { copy(location = event.value) }
            is CreateEventFormEvent.LocationUrlChanged -> update { copy(locationUrl = event.value) }
            is CreateEventFormEvent.MaxPaintingChanged -> update { copy(maxPainting = event.value) }
            is CreateEventFormEvent.MaxPaintingPerArtistChanged -> update { copy(maxPaintingPerArtist = event.value) }
            is CreateEventFormEvent.AboutChanged -> update { copy(about = event.value) }
            is CreateEventFormEvent.TicketPriceChanged -> update { copy(ticketPrice = event.value) }
            is CreateEventFormEvent.ArtistRegistrationFeeChanged -> update { copy(artistRegistrationFee = event.value) }
            is CreateEventFormEvent.EventTypeChanged -> update { copy(eventType = event.value) }
            is CreateEventFormEvent.EventFormatChanged -> update { copy(eventFormat = event.value) }
            is CreateEventFormEvent.PaintingArtistPercentChanged -> update { copy(paintingArtistPercent = event.value) }
            is CreateEventFormEvent.PaintingEventPercentChanged -> update { copy(paintingEventPercent = event.value) }
            is CreateEventFormEvent.PaintingPlatformPercentChanged -> update { copy(paintingPlatformPercent = event.value) }
            is CreateEventFormEvent.TicketArtistPercentChanged -> update { copy(ticketArtistPercent = event.value) }
            is CreateEventFormEvent.TicketEventPercentChanged -> update { copy(ticketEventPercent = event.value) }
            is CreateEventFormEvent.TicketPlatformPercentChanged -> update { copy(ticketPlatformPercent = event.value) }
            CreateEventFormEvent.LoadEventMeta -> loadEventMeta()
            CreateEventFormEvent.Submit -> submit()
        }
    }

    private fun update(block: CreateEventFormState.() -> CreateEventFormState) {
        _formState.update(block)
    }

    private fun loadEventMeta() = viewModelScope.launch {
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
                    errorMessage = "Failed to load metadata",
                    isLoadingMeta = false
                )
            }
        }
    }

    private fun submit() = viewModelScope.launch {
        val state = formState.value
        val bannerUri = state.bannerUri ?: return@launch
        val businessId = savedStateHandle.get<String>("businessId") ?: return@launch

        _uiState.update { it.copy(isLoading = true) }

        val params = CreateEventParams(
            organizerId = businessId,
            title = state.title,
            bannerImageUrl = "", // Will be handled by service during upload
            startDate = state.startDate,
            endDate = state.endDate,
            paintingSubmissionDeadline = state.paintingSubmissionDeadline,
            registrationStartDate = state.registrationStartDate,
            registrationEndDate = state.registrationEndDate,
            about = state.about,
            location = state.location,
            locationUrl = state.locationUrl,
            maxPainting = state.maxPainting?.toIntOrNull(),
            maxPaintingPerArtist = state.maxPaintingPerArtist?.toIntOrNull(),
            ticketPrice = state.ticketPrice?.toDoubleOrNull(),
            registrationFee = state.artistRegistrationFee?.toDoubleOrNull(),
            eventType = state.eventType.toInt(),
            eventFormat = state.eventFormat.toInt(),
            paintingArtistPercent = state.paintingArtistPercent,
            paintingEventPercent = state.paintingEventPercent,
            paintingPlatformPercent = state.paintingPlatformPercent,
            ticketArtistPercent = state.ticketArtistPercent,
            ticketEventPercent = state.ticketEventPercent,
            ticketPlatformPercent = state.ticketPlatformPercent
        )

        when (val result = createEventUseCase(params, bannerUri)) {
            is DataResult.Success -> {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                _effect.emit(CreateEventEffect.NavigateBack)
            }
            is DataResult.Error -> {
                _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
            }
            else -> {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
