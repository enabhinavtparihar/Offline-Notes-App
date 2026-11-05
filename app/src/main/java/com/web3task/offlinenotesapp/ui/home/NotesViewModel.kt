package com.web3task.offlinenotesapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.web3task.offlinenotesapp.data.local.Note
import com.web3task.offlinenotesapp.data.repository.NotesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class NoteSortOrder {
    NEWEST,
    TITLE
}

class NotesViewModel(
    private val repository: NotesRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val sortOrder =
        MutableStateFlow(NoteSortOrder.NEWEST)

    val notes: StateFlow<List<Note>> =
        combine(searchQuery, sortOrder) { query, sort ->
            query to sort
        }.flatMapLatest { (query, sort) ->
            if (sort == NoteSortOrder.TITLE && query.isBlank()) {
                repository.getNotesSortedByTitle()
            } else {
                repository.searchNotes(query).let { flow ->
                    if (sort == NoteSortOrder.TITLE) {
                        flow.map {
                            it.sortedBy { note ->
                                note.title.lowercase()
                            }
                        }
                    } else {
                        flow
                    }
                }
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun onSearchChange(query: String) {
        searchQuery.value = query
    }

    fun changeSortOrder(order: NoteSortOrder) {
        sortOrder.value = order
    }

    fun saveNote(note: Note) {
        viewModelScope.launch {
            if (note.title.isBlank() && note.content.isBlank()) {
                return@launch
            }

            val savedNote = note.copy(
                title = note.title.ifBlank { "Untitled note" },
                updatedAt = System.currentTimeMillis()
            )

            if (savedNote.id == 0L) {
                repository.insertNote(savedNote)
            } else {
                repository.updateNote(savedNote)
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
}
