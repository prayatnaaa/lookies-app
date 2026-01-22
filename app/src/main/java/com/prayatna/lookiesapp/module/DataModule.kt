package com.prayatna.lookiesapp.module

import com.prayatna.lookiesapp.data.repository.AdminRepositoryImpl
import com.prayatna.lookiesapp.data.repository.ArtistRepositoryImpl
import com.prayatna.lookiesapp.data.repository.AuthRepositoryImpl
import com.prayatna.lookiesapp.data.repository.ChatRepositoryImpl
import com.prayatna.lookiesapp.data.repository.EventRepositoryImpl
import com.prayatna.lookiesapp.data.repository.LocationRepositoryImpl
import com.prayatna.lookiesapp.data.repository.PaintingRepositoryImpl
import com.prayatna.lookiesapp.data.repository.PartnerRepositoryImpl
import com.prayatna.lookiesapp.data.repository.PaymentRepositoryImpl
import com.prayatna.lookiesapp.data.repository.TransactionRepositoryImpl
import com.prayatna.lookiesapp.data.repository.UserRepositoryImpl
import com.prayatna.lookiesapp.domain.repository.AdminRepository
import com.prayatna.lookiesapp.domain.repository.ArtistRepository
import com.prayatna.lookiesapp.domain.repository.AuthRepository
import com.prayatna.lookiesapp.domain.repository.ChatRepository
import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.domain.repository.LocationRepository
import com.prayatna.lookiesapp.domain.repository.PaintingRepository
import com.prayatna.lookiesapp.domain.repository.PartnerRepository
import com.prayatna.lookiesapp.domain.repository.PaymentRepository
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Singleton
    @Binds
    fun bindsAuthRepository(authRepository: AuthRepositoryImpl): AuthRepository

    @Singleton
    @Binds
    fun bindsUserRepository(userRepository: UserRepositoryImpl): UserRepository

    @Singleton
    @Binds
    fun bindsEventRepository(eventRepository: EventRepositoryImpl): EventRepository

    @Singleton
    @Binds
    fun bindsPaymentRepository(paymentRepository: PaymentRepositoryImpl): PaymentRepository

    @Singleton
    @Binds
    fun bindsLocationRepository(locationRepository: LocationRepositoryImpl): LocationRepository

    @Singleton
    @Binds
    fun bindsPartnerRepository(partnerRepository: PartnerRepositoryImpl): PartnerRepository

    @Singleton
    @Binds
    fun bindsAdminRepository(adminRepository: AdminRepositoryImpl): AdminRepository

    @Singleton
    @Binds
    fun bindsPaintingRepository(paintingRepository: PaintingRepositoryImpl): PaintingRepository

    @Singleton
    @Binds
    fun artistPaintingRepository(artistRepositoryImpl: ArtistRepositoryImpl): ArtistRepository

    @Singleton
    @Binds
    fun transactionRepository(transactionRepositoryImpl: TransactionRepositoryImpl): TransactionRepository

    @Singleton
    @Binds
    fun chatRepository(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository
}