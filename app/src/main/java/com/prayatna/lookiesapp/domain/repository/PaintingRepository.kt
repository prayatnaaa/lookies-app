package com.prayatna.lookiesapp.domain.repository

import android.net.Uri
import com.prayatna.lookiesapp.domain.model.painting.AddPaintingParams
import com.prayatna.lookiesapp.domain.model.painting.Painting
import com.prayatna.lookiesapp.utils.DataResult

interface PaintingRepository {
    suspend fun getPaintings(): DataResult<List<Painting>>
    suspend fun getPaintingsByArtist(): DataResult<List<Painting>>
    suspend fun getPaintingDetail(id: Int): DataResult<Painting>
    suspend fun uploadPainting(painting: AddPaintingParams, image: Uri): DataResult<Painting>
    suspend fun deletePainting(paintingId: String): DataResult<String>
    suspend fun editPainting(painting: Painting): DataResult<String>
}