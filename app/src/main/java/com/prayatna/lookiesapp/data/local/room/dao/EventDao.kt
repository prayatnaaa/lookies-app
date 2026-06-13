package com.prayatna.lookiesapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.prayatna.lookiesapp.data.local.room.entity.Event
import kotlinx.coroutines.flow.Flow


@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<Event>)

    @Update
    suspend fun updateEvent(event: Event)

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: String): Event?

    @Query("SELECT * FROM events ORDER BY start_date DESC")
    fun getAllEvents(): Flow<List<Event>>
}
