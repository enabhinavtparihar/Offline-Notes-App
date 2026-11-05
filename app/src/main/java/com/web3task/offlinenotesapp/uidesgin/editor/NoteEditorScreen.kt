package com.web3task.offlinenotesapp.uidesgin.editor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.web3task.offlinenotesapp.data.local.Note
import com.web3task.offlinenotesapp.data.repository.NotesRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    noteId: Long,
    repository: NotesRepository,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(noteId != 0L) }
    var errorMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    LaunchedEffect(noteId) {
        if (noteId != 0L) {
            val existingNote = repository.getNoteById(noteId)

            if (existingNote != null) {
                title = existingNote.title
                content = existingNote.content
            }

            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (noteId == 0L) "New Note" else "Edit Note")
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Cancel")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (title.isBlank() && content.isBlank()) {
                                errorMessage =
                                    "Please enter a title or note content."
                            } else {
                                scope.launch {
                                    val now = System.currentTimeMillis()

                                    val note = if (noteId == 0L) {
                                        Note(
                                            title = title.ifBlank {
                                                "Untitled note"
                                            },
                                            content = content,
                                            createdAt = now,
                                            updatedAt = now
                                        )
                                    } else {
                                        val oldNote =
                                            repository.getNoteById(noteId)

                                        if (oldNote == null) {
                                            errorMessage =
                                                "Note not found."
                                            return@launch
                                        }

                                        oldNote.copy(
                                            title = title.ifBlank {
                                                "Untitled note"
                                            },
                                            content = content,
                                            updatedAt = now
                                        )
                                    }

                                    if (noteId == 0L) {
                                        repository.insertNote(note)
                                    } else {
                                        repository.updateNote(note)
                                    }

                                    onBack()
                                }
                            }
                        }
                    ) {
                        Text("Save")
                    }
                }
            )
        }
    ) { padding ->

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        errorMessage = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Note title")
                    },
                    textStyle = MaterialTheme.typography.headlineSmall,
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = content,
                    onValueChange = {
                        content = it
                        errorMessage = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 350.dp),
                    placeholder = {
                        Text("Start writing your note...")
                    }
                )

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
