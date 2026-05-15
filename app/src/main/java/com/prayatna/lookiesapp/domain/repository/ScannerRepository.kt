package com.prayatna.lookiesapp.domain.repository

import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.flow.Flow

interface ScannerRepository {
    fun scanBarcode(imageProxy: ImageProxy): Flow<List<Barcode>>
}
