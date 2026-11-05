package com.web3task.offlinenotesapp.uidesgin.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.web3task.offlinenotesapp.data.local.Note
import com.web3task.offlinenotesapp.ui.home.NoteSortOrder
import com.web3task.offlinenotesapp.ui.home.NotesViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotesScreen(
    viewModel: NotesViewModel,
    onAddClick: () -> Unit,
    onNoteClick: (Long) -> Unit
) {
    val notes by viewModel.notes.collectAsState()

    var searchText by remember {
        mutableStateOf("")
    }

    var selectedSort by remember {
        mutableStateOf(NoteSortOrder.NEWEST)
    }

    var noteToDelete by remember {
        mutableStateOf<Note?>(null)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick
            ) {
                Text("+")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {

            Text(
                text = "My Notes",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${notes.size} notes saved",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    viewModel.onSearchChange(it)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Search your notes...")
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                FilterChip(
                    selected = selectedSort == NoteSortOrder.NEWEST,
                    onClick = {
                        selectedSort = NoteSortOrder.NEWEST
                        viewModel.changeSortOrder(
                            NoteSortOrder.NEWEST
                        )
                    },
                    label = { Text("Newest first") }
                )

                FilterChip(
                    selected = selectedSort == NoteSortOrder.TITLE,
                    onClick = {
                        selectedSort = NoteSortOrder.TITLE
                        viewModel.changeSortOrder(
                            NoteSortOrder.TITLE
                        )
                    },
                    label = { Text("A - Z") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (notes.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = if (searchText.isBlank())
                                "No notes yet"
                            else
                                "No matching notes found",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (searchText.isBlank())
                                "Create your first note."
                            else
                                "Try searching with another keyword."
                        )

                        if (searchText.isBlank()) {
                            Spacer(modifier = Modifier.height(16.dp))

                            Button(onClick = onAddClick) {
                                Text("Create Note")
                            }
                        }
                    }
                }

            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        items = notes,
                        key = { it.id }
                    ) { note ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onNoteClick(note.id)
                                },
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 2.dp
                            )
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement =
                                        Arrangement.SpaceBetween,
                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = note.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    TextButton(
                                        onClick = {
                                            noteToDelete = note
                                        }
                                    ) {
                                        Text("Delete")
                                    }
                                }

                                Text(
                                    text = note.content,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(
                                    modifier = Modifier.height(10.dp)
                                )

                                Text(
                                    text = "Updated: ${
                                        formatDate(note.updatedAt)
                                    }",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    noteToDelete?.let { note ->

        AlertDialog(
            onDismissRequest = {
                noteToDelete = null
            },
            title = {
                Text("Delete note?")
            },
            text = {
                Text("This note will be permanently deleted.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteNote(note)
                        noteToDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        noteToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

private fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat(
        "dd MMM yyyy, hh:mm a",
        Locale.getDefault()
    )

    return formatter.format(Date(timestamp))
}
