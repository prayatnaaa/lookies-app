package com.prayatna.lookiesapp.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.prayatna.lookiesapp.data.mapper.toDomain
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabasePaintingService
import com.prayatna.lookiesapp.domain.mapper.toDto
import com.prayatna.lookiesapp.domain.model.painting.AddPaintingParams
import com.prayatna.lookiesapp.domain.model.painting.Painting
import com.prayatna.lookiesapp.domain.repository.PaintingRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.compressImage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PaintingRepositoryImpl @Inject constructor(
    private val paintingService: SupabasePaintingService,
    @ApplicationContext private val context: Context
): PaintingRepository {
    override suspend fun getPaintings(): DataResult<List<Painting>> {
        TODO("Not yet implemented")
    }

    override suspend fun getPaintingsByArtist(): DataResult<List<Painting>> {
        TODO("Not yet implemented")
    }

    override suspend fun getPaintingDetail(id: Int): DataResult<Painting> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadPainting(painting: AddPaintingParams, image: Uri): DataResult<Painting> {
        val compressedImage = image.compressImage(context, 500_000L)
        return try {
            val response = paintingService.uploadPainting(painting = painting.toDto(), image = compressedImage)
            Log.d("UPLOAD-PAINTING", response.toString())
            DataResult.Success(response.toDomain())
        } catch (e: Exception) {
            Log.d("UPLOAD-PAINTING", e.message.toString())
            DataResult.Error(e.message ?: "Something went wrong! Please check your connection")
        }
    }

    override suspend fun deletePainting(paintingId: String): DataResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun editPainting(painting: Painting): DataResult<String> {
        TODO("Not yet implemented")
    }
}