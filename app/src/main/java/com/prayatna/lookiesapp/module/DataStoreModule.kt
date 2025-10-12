package com.prayatna.lookiesapp.module

import android.content.Context
import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideUserPreference(@ApplicationContext context: Context): UserPreference {
        return UserPreference(context)
    }
}