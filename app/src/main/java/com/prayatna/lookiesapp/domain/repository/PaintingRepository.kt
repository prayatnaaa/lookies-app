package com.prayatna.lookiesapp.domain.repository

import android.net.Uri
import com.prayatna.lookiesapp.domain.model.painting.AddPaintingParams
import com.prayatna.lookiesapp.domain.model.painting.DetailPainting
import com.prayatna.lookiesapp.domain.model.painting.Painting
import com.prayatna.lookiesapp.domain.model.painting.PaintingAttribute
import com.prayatna.lookiesapp.utils.DataResult

interface PaintingRepository {
    suspend fun getPaintings(): DataResult<List<Painting>>
    suspend fun getPaintingsByArtist(id: String): DataResult<List<Painting>>
    suspend fun getPaintingDetail(id: Int): DataResult<DetailPainting>
    suspend fun uploadPainting(painting: AddPaintingParams, image: Uri): DataResult<Painting>
    suspend fun deletePainting(paintingId: String): DataResult<String>
    suspend fun editPainting(painting: Painting): DataResult<String>
    suspend fun getPaintingArtStyles(): DataResult<List<PaintingAttribute>>
    suspend fun getPaintingMediums(): DataResult<List<PaintingAttribute>>
}