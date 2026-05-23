package com.prayatna.lookiesapp.domain.repository

import android.net.Uri
import com.prayatna.lookiesapp.domain.model.painting.AddPaintingParams
import com.prayatna.lookiesapp.domain.model.painting.BasePainting
import com.prayatna.lookiesapp.domain.model.painting.CreatePaintingReviewInput
import com.prayatna.lookiesapp.domain.model.painting.DetailPainting
import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.domain.model.painting.Painting
import com.prayatna.lookiesapp.domain.model.painting.PaintingAttribute
import com.prayatna.lookiesapp.domain.model.painting.PaintingReview
import com.prayatna.lookiesapp.domain.model.painting.UploadPaintingOutput
import com.prayatna.lookiesapp.utils.DataResult

interface PaintingRepository {
    suspend fun getPaintings(
        id: String?,
        status: String?,
        eventId: String?,
        showSelfPaintings: Boolean = false,
        limitCount: Long? = null
    ): DataResult<List<EventPainting>>
    suspend fun getEventPaintingDetail(id: String): DataResult<EventPainting>
    suspend fun getPaintingsByArtist(id: String, status: String? = null): DataResult<List<Painting>>
    suspend fun getPaintingDetail(id: Int): DataResult<DetailPainting>
    suspend fun uploadPainting(painting: AddPaintingParams, image: Uri): DataResult<UploadPaintingOutput>
    suspend fun deletePainting(paintingId: String): DataResult<String>
    suspend fun editPainting(painting: AddPaintingParams, paintingId: Int, image: Uri?): DataResult<BasePainting>
    suspend fun getPaintingArtStyles(): DataResult<List<PaintingAttribute>>
    suspend fun getPaintingMediums(): DataResult<List<PaintingAttribute>>
    suspend fun updateEventPaintingStatus(eventPaintingId: String, status: String): DataResult<Unit>
    suspend fun createPaintingReview(paintingReview: CreatePaintingReviewInput): DataResult<PaintingReview>
}