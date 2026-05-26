package com.prayatna.lookiesapp.domain.usecase.shipment

import android.util.Log
import com.prayatna.lookiesapp.domain.model.shipment.EventPaintingExhibitionShipment
import com.prayatna.lookiesapp.domain.repository.PaintingRepository
import com.prayatna.lookiesapp.domain.repository.ShipmentRepository
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class GetExhibitionShipmentByEventPaintingIdUseCase @Inject constructor(
    private val shipmentRepository: ShipmentRepository,
    private val paintingRepository: PaintingRepository
) {
    suspend operator fun invoke(eventPaintingId: String): DataResult<EventPaintingExhibitionShipment> {
        return supervisorScope {
            val shipmentDef = async { shipmentRepository.getExhibitionShipmentByEventPaintingId(eventPaintingId) }
            val paintingDef = async { paintingRepository.getEventPaintingDetail(eventPaintingId) }

            val shipment = shipmentDef.await()
            val painting = paintingDef.await()

            if (shipment is DataResult.Success && painting is DataResult.Success) {
                Log.d("GetExhibitionShipmentByEventPaintingIdUseCase", "invoke: ${painting.data}")
                Log.d("GetExhibitionShipmentByEventPaintingIdUseCase", "invoke: ${shipment.data}")
                DataResult.Success(
                    EventPaintingExhibitionShipment(
                        exhibitionShipment = shipment.data,
                        eventPainting = painting.data
                    )
                )
            } else {
                when {
                    shipment is DataResult.Error -> shipment
                    painting is DataResult.Error -> painting
                    else -> DataResult.Error("Something went wrong")
                }
            }
        }
    }
}