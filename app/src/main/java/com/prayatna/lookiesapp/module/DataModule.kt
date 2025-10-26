package com.prayatna.lookiesapp.module

import com.prayatna.lookiesapp.data.repository.ArtistRepository
import com.prayatna.lookiesapp.data.repository.ArtistRepositoryImpl
import com.prayatna.lookiesapp.data.repository.AuthRepository
import com.prayatna.lookiesapp.data.repository.AuthRepositoryImpl
import com.prayatna.lookiesapp.data.repository.EventRepository
import com.prayatna.lookiesapp.data.repository.EventRepositoryImpl
import com.prayatna.lookiesapp.data.repository.PaymentRepository
import com.prayatna.lookiesapp.data.repository.PaymentRepositoryImpl
import com.prayatna.lookiesapp.data.repository.UserRepository
import com.prayatna.lookiesapp.data.repository.UserRepositoryImpl
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
    fun bindsArtistRepository(artistRepository: ArtistRepositoryImpl): ArtistRepository

    @Singleton
    @Binds
    fun bindsUserRepository(userRepository: UserRepositoryImpl): UserRepository

    @Singleton
    @Binds
    fun bindsEventRepository(eventRepository: EventRepositoryImpl): EventRepository

    @Singleton
    @Binds
    fun bindsPaymentRepository(paymentRepository: PaymentRepositoryImpl): PaymentRepository
}