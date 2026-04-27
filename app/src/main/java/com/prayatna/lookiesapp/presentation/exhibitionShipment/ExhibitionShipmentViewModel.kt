package com.prayatna.lookiesapp.presentation.exhibitionShipment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.shipment.CreateExhibitionShipmentInput
import com.prayatna.lookiesapp.domain.usecase.shipment.CreateExhibitionShipmentUseCase
import com.prayatna.lookiesapp.domain.model.shipment.DeliveryMethod
import com.prayatna.lookiesapp.domain.model.shipment.ShipmentStatus
import com.prayatna.lookiesapp.domain.model.shipment.ShipmentType
import com.prayatna.lookiesapp.domain.usecase.shipment.UpdateExhibitionShipmentStatusUseCase
import com.prayatna.lookiesapp.domain.usecase.shipment.GetExhibitionShipmentByEventPaintingIdUseCase
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
//    private val updateEventPaintingStatusUseCase: UpdateEventPaintingStatusUseCase,
    private val getExhibitionShipmentByEventPaintingIdUseCase: GetExhibitionShipmentByEventPaintingIdUseCase
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
            when (val result = getExhibitionShipmentByEventPaintingIdUseCase(eventPaintingId)) {
                is DataResult.Success -> {
                    val data = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            shipment = data.exhibitionShipment,
                            eventPainting = data.eventPainting,
                            artistId = data.eventPainting.artistId,
                            organizerId = data.eventPainting.participant.event.organizer.id,
                            eventId = data.eventPainting.eventId.toInt(),
                            eventPaintingStatus = data.eventPainting.status,
                            paintingTitle = data.eventPainting.painting.title,
                            eventTitle = data.eventPainting.participant.event.title,
                            eventLocation = data.eventPainting.participant.event.location,
                            organizerName = data.eventPainting.participant.event.organizer.legalName,

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
                eventPaintingId = UUID.fromString(state.eventPainting?.id),
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
                    _uiState.update { it.copy(successMessage = "Inbound shipment created successfully") }

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
        val shipmentId = state.shipment?.id?.toString() ?: run {
            _uiState.update { it.copy(errorMessage = "No shipment record found") }
            return
        }
        Log.d("ExhibitionShipmentViewModel", "confirmArtworkReceived: $shipmentId")

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }

            val shipmentResult = updateExhibitionShipmentStatusUseCase(
                shipmentId = shipmentId,
                notes = state.notesInput.takeIf { it.isNotBlank() },
                status = "received_in_gallery"
            )
            when (shipmentResult) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(successMessage = "Artwork received successfully") }
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
                eventPaintingId = UUID.fromString(state.eventPainting?.id),
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
                    _uiState.update { it.copy(successMessage = "Return shipment created successfully") }
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
                    _uiState.update { it.copy(successMessage = "Artwork returned successfully") }
                }
                is DataResult.Error -> _uiState.update {
                    it.copy(isSubmitting = false, errorMessage = shipmentResult.error)
                }
                else -> Unit
            }
        }
    }
}
