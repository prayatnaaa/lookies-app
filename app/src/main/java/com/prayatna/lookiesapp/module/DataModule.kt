package com.prayatna.lookiesapp.module

import com.prayatna.lookiesapp.data.repository.ArtistRepository
import com.prayatna.lookiesapp.data.repository.ArtistRepositoryImpl
import com.prayatna.lookiesapp.data.repository.AuthRepository
import com.prayatna.lookiesapp.data.repository.AuthRepositoryImpl
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
}