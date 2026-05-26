package com.prayatna.lookiesapp.domain.usecase.scanner

import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.common.Barcode
import com.prayatna.lookiesapp.domain.repository.ScannerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScanBarcodeUseCase @Inject constructor(
    private val repository: ScannerRepository
) {
    operator fun invoke(imageProxy: ImageProxy): Flow<List<Barcode>> {
        return repository.scanBarcode(imageProxy)
    }
}
