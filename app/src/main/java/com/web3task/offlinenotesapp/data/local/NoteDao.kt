
package com.web3task.offlinenotesapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("""
        SELECT * FROM notes
        WHERE title LIKE '%' || :searchQuery || '%'
        OR content LIKE '%' || :searchQuery || '%'
        ORDER BY updatedAt DESC
    """)
    fun searchNotes(searchQuery: String): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Long): Note?

    @Query("SELECT * FROM notes ORDER BY title COLLATE NOCASE ASC")
    fun getNotesSortedByTitle(): Flow<List<Note>>
}
