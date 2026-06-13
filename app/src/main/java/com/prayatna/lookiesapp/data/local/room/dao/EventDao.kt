package com.prayatna.lookiesapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
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

    @Query("SELECT * FROM events WHERE id = :id")
    fun getEventByIdFlow(id: String): Flow<Event?>

    @Query("SELECT * FROM events ORDER BY start_date DESC")
    fun getAllEvents(): Flow<List<Event>>

    @Query("SELECT * FROM events ORDER BY start_date DESC")
    suspend fun getEventsSync(): List<Event>

    /**
     * Best Practice: Robust filtered query for offline fallback.
     * Supports search by title and filtering by status, type, and format.
     */
    @Query("""
    SELECT * FROM events
    WHERE (:title IS NULL OR title LIKE '%' || :title || '%')
    AND status IN (:statuses)
    AND (:eventType IS NULL OR event_type_name = :eventType)
    AND (:eventFormat IS NULL OR event_format_name = :eventFormat)
    ORDER BY
    CASE WHEN :isAsc = 1 THEN ticket_price END ASC,
    CASE WHEN :isAsc = 0 THEN ticket_price END DESC,
    start_date DESC
""")
    fun getFilteredEventsWithStatusFlow(
        title: String?,
        statuses: List<String>,
        eventType: String?,
        eventFormat: String?,
        isAsc: Int
    ): Flow<List<Event>>

    @Query("""
    SELECT * FROM events
    WHERE (:title IS NULL OR title LIKE '%' || :title || '%')
    AND (:eventType IS NULL OR event_type_name = :eventType)
    AND (:eventFormat IS NULL OR event_format_name = :eventFormat)
    ORDER BY
    CASE WHEN :isAsc = 1 THEN ticket_price END ASC,
    CASE WHEN :isAsc = 0 THEN ticket_price END DESC,
    start_date DESC
""")
    fun getFilteredEventsFlow(
        title: String?,
        eventType: String?,
        eventFormat: String?,
        isAsc: Int
    ): Flow<List<Event>>

    @Query("DELETE FROM events")
    suspend fun clearAllEvents()

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEventById(id: String)

    @Transaction
    suspend fun syncEvents(events: List<Event>) {
        // We use REPLACE strategy in insertEvents, so this keeps things up to date.
        insertEvents(events)
    }
}
