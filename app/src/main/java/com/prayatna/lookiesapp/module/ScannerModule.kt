package com.prayatna.lookiesapp.module

import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.prayatna.lookiesapp.data.repository.scanner.ScannerRepositoryImpl
import com.prayatna.lookiesapp.domain.repository.ScannerRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ScannerModule {

    @Binds
    @Singleton
    abstract fun bindScannerRepository(
        scannerRepositoryImpl: ScannerRepositoryImpl
    ): ScannerRepository

    companion object {
        @Provides
        @Singleton
        fun provideBarcodeScanner(): BarcodeScanner {
            return BarcodeScanning.getClient()
        }
    }
}
