package com.prayatna.lookiesapp.data.repository.scanner

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.prayatna.lookiesapp.domain.repository.ScannerRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ScannerRepositoryImpl @Inject constructor(
    private val scanner: BarcodeScanner
) : ScannerRepository {

    @OptIn(ExperimentalGetImage::class)
    override fun scanBarcode(imageProxy: ImageProxy): Flow<List<Barcode>> = callbackFlow {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    trySend(barcodes)
                }
                .addOnFailureListener {
                    close(it)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
            trySend(emptyList())
        }
        awaitClose { /* No cleanup needed for scanner process */ }
    }
}
