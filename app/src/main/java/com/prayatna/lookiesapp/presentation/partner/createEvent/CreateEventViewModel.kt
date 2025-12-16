package com.prayatna.lookiesapp.presentation.partner.createEvent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.event.CreateEventParams
import com.prayatna.lookiesapp.domain.usecase.event.CreateEventUseCase
import com.prayatna.lookiesapp.presentation.partner.createEvent.state.CreateEventFormEvent
import com.prayatna.lookiesapp.presentation.partner.createEvent.state.CreateEventFormState
import com.prayatna.lookiesapp.presentation.partner.createEvent.state.CreateEventUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val createEventUseCase: CreateEventUseCase
) : ViewModel() {

    private val _formState = MutableStateFlow(CreateEventFormState())
    val formState: StateFlow<CreateEventFormState> = _formState

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

            CreateEventFormEvent.Submit -> submit()
            is CreateEventFormEvent.ArtistRegistrationFeeChanged ->
                update { copy(artistRegistrationFee = event.value) }

            is CreateEventFormEvent.TicketPriceChanged ->
                update { copy(ticketPrice = event.value) }
        }
    }

    private fun update(reducer: CreateEventFormState.() -> CreateEventFormState) {
        _formState.value = _formState.value.reducer()
    }

    private fun submit() {
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
                maxParticipant = state.maxParticipant.toInt(),
                maxPainting = state.maxPainting.toInt(),
                maxPaintingPerArtist = state.maxPaintingPerArtist.toInt(),
                ticketPrice = state.ticketPrice.toDouble(),
                registrationFee = state.artistRegistrationFee.toDouble()
            )


            when(val result = createEventUseCase(params = params, imageUri = state.bannerUri!!)) {
                is DataResult.Error -> {
                    _uiState.update { it.copy(errorMessage = result.error) }
                }
                is DataResult.Success -> {
                    _uiState.update { it.copy(isSuccess = true) }
                }
                is DataResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                else -> Unit
            }
        }
    }
}
