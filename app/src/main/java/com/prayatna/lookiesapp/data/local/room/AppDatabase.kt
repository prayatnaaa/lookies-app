package com.prayatna.lookiesapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.prayatna.lookiesapp.data.local.room.dao.EventDao
import com.prayatna.lookiesapp.data.local.room.entity.Event


@Database(
    entities = [Event::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}