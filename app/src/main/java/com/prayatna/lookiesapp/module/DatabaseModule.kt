package com.prayatna.lookiesapp.module

import android.content.Context
import androidx.room.Room
import com.prayatna.lookiesapp.data.local.room.AppDatabase
import com.prayatna.lookiesapp.data.local.room.dao.EventDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideEventDao(appDatabase: AppDatabase): EventDao {
        return appDatabase.eventDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {

        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "lookies_db"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }
}