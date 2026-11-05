package com.web3task.offlinenotesapp.data.repository

import com.web3task.offlinenotesapp.data.local.Note
import com.web3task.offlinenotesapp.data.local.NoteDao
import kotlinx.coroutines.flow.Flow

class NotesRepository(
    private val noteDao: NoteDao
) {

    fun getAllNotes(): Flow<List<Note>> =
        noteDao.getAllNotes()

    fun searchNotes(query: String): Flow<List<Note>> =
        noteDao.searchNotes(query)

    fun getNotesSortedByTitle(): Flow<List<Note>> =
        noteDao.getNotesSortedByTitle()

    suspend fun getNoteById(id: Long): Note? =
        noteDao.getNoteById(id)

    suspend fun insertNote(note: Note) =
        noteDao.insertNote(note)

    suspend fun updateNote(note: Note) =
        noteDao.updateNote(note)

    suspend fun deleteNote(note: Note) =
        noteDao.deleteNote(note)
}
