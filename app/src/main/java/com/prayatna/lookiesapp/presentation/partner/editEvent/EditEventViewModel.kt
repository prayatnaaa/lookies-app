package com.prayatna.lookiesapp.presentation.partner.editEvent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.mapper.toEditEventInput
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.model.event.EventRevenueRules
import com.prayatna.lookiesapp.domain.model.event.UpdateRevenueRulesInput
import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.domain.usecase.event.EditEventUseCase
import com.prayatna.lookiesapp.domain.usecase.event.GetRevenueRulesByEventIdUseCase
import com.prayatna.lookiesapp.domain.usecase.event.UpdateEventRevenueRulesUseCase
import com.prayatna.lookiesapp.presentation.partner.editEvent.state.EditEventFormEvent
import com.prayatna.lookiesapp.presentation.partner.editEvent.state.EditEventFormState
import com.prayatna.lookiesapp.presentation.partner.editEvent.state.EditEventUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditEventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val editEventUseCase: EditEventUseCase,
    private val getRevenueRulesByEventIdUseCase: GetRevenueRulesByEventIdUseCase,
    private val updateEventRevenueRulesUseCase: UpdateEventRevenueRulesUseCase
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
            is EditEventFormEvent.PaintingSubmissionDeadline ->
                update { copy(paintingSubmissionDeadline = event.value) }

            is EditEventFormEvent.PaintingArtistPercentChanged ->
                update { copy(paintingArtistPercent = event.value) }
            is EditEventFormEvent.PaintingEventPercentChanged ->
                update { copy(paintingEventPercent = event.value) }
            is EditEventFormEvent.PaintingPlatformPercentChanged ->
                update { copy(paintingPlatformPercent = event.value) }
            is EditEventFormEvent.TicketArtistPercentChanged ->
                update { copy(ticketArtistPercent = event.value) }
            is EditEventFormEvent.TicketEventPercentChanged ->
                update { copy(ticketEventPercent = event.value) }
            is EditEventFormEvent.TicketPlatformPercentChanged ->
                update { copy(ticketPlatformPercent = event.value) }
        }
    }

    fun loadEvent(eventId: String) = viewModelScope.launch {
        this@EditEventViewModel.eventId = eventId
        _uiState.update { it.copy(isLoading = true) }

        val eventResult = eventRepository.getEvent(eventId)
        val rulesResult = getRevenueRulesByEventIdUseCase(eventId.toInt())

        if (eventResult is DataResult.Success) {
            val rules = if (rulesResult is DataResult.Success) rulesResult.data else emptyList()
            prefill(eventResult.data, rules)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    data = eventResult.data,
                )
            }
        } else if (eventResult is DataResult.Error) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = eventResult.error
                )
            }
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

    private fun prefill(event: Event, rules: List<EventRevenueRules>) {
        val paintingRule = rules.find { it.itemType == "painting" }
        val ticketRule = rules.find { it.itemType == "ticket" }

        _formState.update {
            it.copy(
                bannerImage = event.bannerImageUrl,
                title = event.title,
                startDate = event.startDate,
                endDate = event.endDate,
                location = event.location ,
                locationUrl = event.locationUrl,
                maxParticipant = event.maxParticipant?.toString(),
                maxPainting = event.maxPainting?.toString(),
                maxPaintingPerArtist = event.maxPaintingPerArtist?.toString(),
                ticketPrice = event.ticketPrice?.toString(),
                artistRegistrationFee = event.artistRegistrationFee?.toString(),
                about = event.about.orEmpty(),
                eventType = event.eventType.id.toString(),
                eventFormat = event.eventFormat.id.toString(),
                paintingSubmissionDeadline = event.paintingSubmissionDeadline,
                paintingArtistPercent = paintingRule?.artistPercent ?: 0,
                paintingEventPercent = paintingRule?.eventPercent ?: 0,
                paintingPlatformPercent = paintingRule?.platformPercent ?: 0,
                paintingRuleId = paintingRule?.id,
                ticketArtistPercent = ticketRule?.artistPercent ?: 0,
                ticketEventPercent = ticketRule?.eventPercent ?: 0,
                ticketPlatformPercent = ticketRule?.platformPercent ?: 0,
                ticketRuleId = ticketRule?.id
            )
        }
    }

    private fun submit() = viewModelScope.launch {
        val id = eventId ?: return@launch
        val state = formState.value

        if (!state.isPaintingRevenueValid) {
            _uiState.update { it.copy(errorMessage = "Painting revenue splits must total 100%") }
            return@launch
        }

        val selectedEventFormat = state.eventFormats.find { it.id.toString() == state.eventFormat }
        if (selectedEventFormat?.slug != "online" && !state.isTicketRevenueValid) {
            _uiState.update { it.copy(errorMessage = "Ticket revenue splits must total 100%") }
            return@launch
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        val currentStatus = uiState.value.data?.status.orEmpty()

        val input = state.toEditEventInput(
            currentStatus = currentStatus
        )

        // 1. Update Core Event Details
        val eventResult = editEventUseCase(id, input)

        if (eventResult is DataResult.Error) {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Event Update Failed: ${eventResult.error}") }
            return@launch
        }

        // 2. Update Revenue Rules in Parallel
        coroutineScope {
            val paintingDeferred = state.paintingRuleId?.let { ruleId ->
                async {
                    updateEventRevenueRulesUseCase(
                        ruleId,
                        UpdateRevenueRulesInput(
                            itemType = "painting",
                            artistPercent = state.paintingArtistPercent,
                            eventPercent = state.paintingEventPercent,
                            platformPercent = state.paintingPlatformPercent,
                            eventId = id.toInt()
                        )
                    )
                }
            }

            val ticketDeferred = state.ticketRuleId?.let { ruleId ->
                async {
                    updateEventRevenueRulesUseCase(
                        ruleId,
                        UpdateRevenueRulesInput(
                            itemType = "ticket",
                            artistPercent = state.ticketArtistPercent,
                            eventPercent = state.ticketEventPercent,
                            platformPercent = state.ticketPlatformPercent,
                            eventId = id.toInt()
                        )
                    )
                }
            }

            val paintingResult = paintingDeferred?.await()
            val ticketResult = ticketDeferred?.await()

            // Handle specific failures
            val errors = mutableListOf<String>()
            if (paintingResult is DataResult.Error) errors.add("Painting Splits: ${paintingResult.error}")
            if (ticketResult is DataResult.Error) errors.add("Ticket Splits: ${ticketResult.error}")

            if (errors.isNotEmpty()) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Some updates failed:\n" + errors.joinToString("\n")
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
            }
        }
    }

}
