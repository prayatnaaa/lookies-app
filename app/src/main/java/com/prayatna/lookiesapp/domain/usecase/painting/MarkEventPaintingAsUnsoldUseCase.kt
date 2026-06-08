package com.prayatna.lookiesapp.domain.usecase.painting

import com.prayatna.lookiesapp.domain.repository.PaintingRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class MarkEventPaintingAsUnsoldUseCase @Inject constructor(
    private val paintingRepository: PaintingRepository
) {
    suspend operator fun invoke(eventPaintingId: String): DataResult<Unit> {
        if (eventPaintingId.isBlank()) return DataResult.Error("Invalid painting ID")

        // 1. Fetch current detail to verify status and event format (Safety check against race conditions)
        val currentDetailResult = paintingRepository.getEventPaintingDetail(eventPaintingId)
        
        if (currentDetailResult is DataResult.Error) return DataResult.Error(currentDetailResult.error)
        if (currentDetailResult is DataResult.Success) {
            val painting = currentDetailResult.data
            
            // Check: Must be 'on_sale'
            if (painting.status.lowercase() != "on_sale") {
                return DataResult.Error("Only artworks currently 'On Sale' can be marked as unsold. Current status: ${painting.status}")
            }
            
            // Check: Must be 'online' event format
            if (painting.participant.event.eventFormat.slug != "online") {
                return DataResult.Error("This action is only available for online events.")
            }
            
            // 2. Perform the update
            return paintingRepository.updateEventPaintingStatus(eventPaintingId, "unsold")
        }
        
        return DataResult.Error("Failed to verify artwork status")
    }
}
