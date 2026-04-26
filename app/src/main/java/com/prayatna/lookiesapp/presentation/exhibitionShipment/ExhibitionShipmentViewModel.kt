package com.prayatna.lookiesapp.presentation.exhibitionShipment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.shipment.CreateExhibitionShipmentInput
import com.prayatna.lookiesapp.domain.shipment.CreateExhibitionShipmentUseCase
import com.prayatna.lookiesapp.domain.shipment.DeliveryMethod
import com.prayatna.lookiesapp.domain.shipment.ShipmentStatus
import com.prayatna.lookiesapp.domain.shipment.ShipmentType
import com.prayatna.lookiesapp.domain.shipment.UpdateExhibitionShipmentStatusUseCase
import com.prayatna.lookiesapp.domain.usecase.painting.GetEventPaintingByIdUseCase
import com.prayatna.lookiesapp.domain.usecase.painting.UpdateEventPaintingStatusUseCase
import com.prayatna.lookiesapp.presentation.exhibitionShipment.state.ExhibitionShipmentEvent
import com.prayatna.lookiesapp.presentation.exhibitionShipment.state.ExhibitionShipmentUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ExhibitionShipmentViewModel @Inject constructor(
    private val createExhibitionShipmentUseCase: CreateExhibitionShipmentUseCase,
    private val updateExhibitionShipmentStatusUseCase: UpdateExhibitionShipmentStatusUseCase,
    private val updateEventPaintingStatusUseCase: UpdateEventPaintingStatusUseCase,
    private val getEventPaintingByIdUseCase: GetEventPaintingByIdUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExhibitionShipmentUiState())
    val uiState: StateFlow<ExhibitionShipmentUiState> = _uiState.asStateFlow()

    /**
     * Fetches the EventPainting by ID and populates all contextual state.
     * The caller only needs to supply the eventPaintingId.
     */
    fun init(eventPaintingId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = getEventPaintingByIdUseCase(eventPaintingId)) {
                is DataResult.Success -> {
                    val ep = result.data
                    val event = ep.participant.event
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            eventPaintingId = ep.id,
                            artistId = ep.artistId,
                            organizerId = event.organizer.id,
                            eventId = event.id.toIntOrNull() ?: 0,
                            eventPaintingStatus = ep.status,
                            paintingTitle = ep.painting.title,
                            eventTitle = event.title,
                            eventLocation = event.location,
                            organizerName = event.organizer.legalName,
                            eventStartDate = event.startDate.take(10),
                            eventEndDate = event.endDate.take(10),
                        )
                    }
                }
                is DataResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error)
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onEvent(event: ExhibitionShipmentEvent) {
        when (event) {

            // ── Form field bindings ──────────────────────────────────────────
            is ExhibitionShipmentEvent.OnDeliveryMethodSelected ->
                _uiState.update { it.copy(selectedDeliveryMethod = event.method) }

            is ExhibitionShipmentEvent.OnCourierNameChanged ->
                _uiState.update { it.copy(courierNameInput = event.name) }

            is ExhibitionShipmentEvent.OnTrackingNumberChanged ->
                _uiState.update { it.copy(trackingNumberInput = event.trackingNumber) }

            is ExhibitionShipmentEvent.OnGalleryNotesChanged ->
                _uiState.update { it.copy(notesInput = event.notes) }

            is ExhibitionShipmentEvent.OnReturnTrackingNumberChanged ->
                _uiState.update { it.copy(returnTrackingNumberInput = event.trackingNumber) }

            is ExhibitionShipmentEvent.OnArtistConditionNotesChanged ->
                _uiState.update { it.copy(notesInput = event.notes) }

            // ── Business actions ─────────────────────────────────────────────
            ExhibitionShipmentEvent.SubmitInboundShipment -> submitInboundShipment()
            ExhibitionShipmentEvent.ConfirmArtworkReceived -> confirmArtworkReceived()
            ExhibitionShipmentEvent.SubmitReturnShipment -> submitReturnShipment()
            ExhibitionShipmentEvent.ConfirmArtworkReturned -> confirmArtworkReturned()

            // ── Navigation / dialog ──────────────────────────────────────────
            ExhibitionShipmentEvent.DismissError ->
                _uiState.update { it.copy(errorMessage = null) }

            ExhibitionShipmentEvent.DismissSuccess ->
                _uiState.update { it.copy(successMessage = null) }

            ExhibitionShipmentEvent.OnBackClick -> { /* handled in Route */ }
        }
    }

    // ── Inbound Phase ────────────────────────────────────────────────────────

    /**
     * Artist fills shipment details and submits → creates an INBOUND ExhibitionShipment record
     * and updates event_paintings.status to "shipping_to_event".
     */
    private fun submitInboundShipment() {
        val state = _uiState.value

        if (state.selectedDeliveryMethod == DeliveryMethod.COURIER && state.courierNameInput.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter the courier name") }
            return
        }
        if (state.selectedDeliveryMethod == DeliveryMethod.COURIER && state.trackingNumberInput.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter the tracking number") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }

            val input = CreateExhibitionShipmentInput(
                eventPaintingId = UUID.fromString(state.eventPaintingId),
                artistId = UUID.fromString(state.artistId),
                organizerId = UUID.fromString(state.organizerId),
                shipmentType = ShipmentType.INBOUND,
                deliveryMethod = state.selectedDeliveryMethod,
                courierName = state.courierNameInput.takeIf { it.isNotBlank() },
                trackingNumber = state.trackingNumberInput.takeIf { it.isNotBlank() },
                status = ShipmentStatus.ON_THE_WAY,
                eventId = state.eventId,
            )

            when (val shipmentResult = createExhibitionShipmentUseCase(input)) {
                is DataResult.Success -> {
                    // Also update event_painting status to shipping_to_event
                    val statusResult = updateEventPaintingStatusUseCase(
                        eventPaintingId = state.eventPaintingId,
                        status = "shipping_to_event"
                    )
                    when (statusResult) {
                        is DataResult.Success -> _uiState.update {
                            it.copy(
                                isSubmitting = false,
                                shipment = shipmentResult.data,
                                eventPaintingStatus = "shipping_to_event",
                                successMessage = "Shipment submitted! The organizer will verify arrival."
                            )
                        }
                        is DataResult.Error -> _uiState.update {
                            it.copy(isSubmitting = false, errorMessage = statusResult.error)
                        }
                        else -> Unit
                    }
                }
                is DataResult.Error -> _uiState.update {
                    it.copy(isSubmitting = false, errorMessage = shipmentResult.error)
                }
                else -> Unit
            }
        }
    }

    /**
     * Organizer confirms the artwork arrived at the gallery →
     * updates ExhibitionShipment status to RECEIVED_IN_GALLERY and
     * event_paintings.status to "exhibited".
     */
    private fun confirmArtworkReceived() {
        val state = _uiState.value
        Log.d("ExhibitionShipmentViewModel", "confirmArtworkReceived: $state")
        val shipmentId = state.shipment?.id?.toString() ?: run {
            _uiState.update { it.copy(errorMessage = "No shipment record found") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }

            val shipmentResult = updateExhibitionShipmentStatusUseCase(
                shipmentId = shipmentId,
                notes = state.notesInput.takeIf { it.isNotBlank() },
                status = "received_in_gallery"
            )
            when (shipmentResult) {
                is DataResult.Success -> {
                    val statusResult = updateEventPaintingStatusUseCase(
                        eventPaintingId = state.eventPaintingId,
                        status = "exhibited"
                    )
                    when (statusResult) {
                        is DataResult.Success -> _uiState.update {
                            it.copy(
                                isSubmitting = false,
                                shipment = shipmentResult.data,
                                eventPaintingStatus = "exhibited",
                                notesInput = "",
                                successMessage = "Artwork marked as exhibited and ready to display!"
                            )
                        }
                        is DataResult.Error -> _uiState.update {
                            it.copy(isSubmitting = false, errorMessage = statusResult.error)
                        }
                        else -> Unit
                    }
                }
                is DataResult.Error -> _uiState.update {
                    it.copy(isSubmitting = false, errorMessage = shipmentResult.error)
                }
                else -> Unit
            }
        }
    }

    // ── Outbound Phase ───────────────────────────────────────────────────────

    /**
     * Organizer sets up return shipment (artwork is unsold) →
     * creates a RETURN ExhibitionShipment and updates event_paintings.status to "returning_to_creator".
     */
    private fun submitReturnShipment() {
        val state = _uiState.value

        if (state.returnTrackingNumberInput.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter the return tracking number") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }

            val input = CreateExhibitionShipmentInput(
                eventPaintingId = UUID.fromString(state.eventPaintingId),
                artistId = UUID.fromString(state.artistId),
                organizerId = UUID.fromString(state.organizerId),
                shipmentType = ShipmentType.RETURN,
                deliveryMethod = DeliveryMethod.COURIER,
                trackingNumber = state.returnTrackingNumberInput,
                courierName = state.courierNameInput.takeIf { it.isNotBlank() },
                status = ShipmentStatus.ON_THE_WAY,
                eventId = state.eventId,
            )

            when (val shipmentResult = createExhibitionShipmentUseCase(input)) {
                is DataResult.Success -> {
                    val statusResult = updateEventPaintingStatusUseCase(
                        eventPaintingId = state.eventPaintingId,
                        status = "returning_to_creator"
                    )
                    when (statusResult) {
                        is DataResult.Success -> _uiState.update {
                            it.copy(
                                isSubmitting = false,
                                shipment = shipmentResult.data,
                                eventPaintingStatus = "returning_to_creator",
                                successMessage = "Return shipment created. Artist will confirm receipt."
                            )
                        }
                        is DataResult.Error -> _uiState.update {
                            it.copy(isSubmitting = false, errorMessage = statusResult.error)
                        }
                        else -> Unit
                    }
                }
                is DataResult.Error -> _uiState.update {
                    it.copy(isSubmitting = false, errorMessage = shipmentResult.error)
                }
                else -> Unit
            }
        }
    }

    /**
     * Artist confirms receipt of the returned artwork →
     * updates ExhibitionShipment status to RETURNED_TO_ARTIST and
     * event_paintings.status to "returned".
     */
    private fun confirmArtworkReturned() {
        val state = _uiState.value
        val shipmentId = state.shipment?.id?.toString() ?: run {
            _uiState.update { it.copy(errorMessage = "No return shipment record found") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }

            val shipmentResult = updateExhibitionShipmentStatusUseCase(
                shipmentId = shipmentId,
                notes = state.notesInput.takeIf { it.isNotBlank() },
                status = "returned_to_artist"
            )
            when (shipmentResult) {
                is DataResult.Success -> {
                    val statusResult = updateEventPaintingStatusUseCase(
                        eventPaintingId = state.eventPaintingId,
                        status = "returned"
                    )
                    when (statusResult) {
                        is DataResult.Success -> _uiState.update {
                            it.copy(
                                isSubmitting = false,
                                shipment = shipmentResult.data,
                                eventPaintingStatus = "returned",
                                notesInput = "",
                                successMessage = "Artwork returned successfully. The cycle is complete!"
                            )
                        }
                        is DataResult.Error -> _uiState.update {
                            it.copy(isSubmitting = false, errorMessage = statusResult.error)
                        }
                        else -> Unit
                    }
                }
                is DataResult.Error -> _uiState.update {
                    it.copy(isSubmitting = false, errorMessage = shipmentResult.error)
                }
                else -> Unit
            }
        }
    }
}
