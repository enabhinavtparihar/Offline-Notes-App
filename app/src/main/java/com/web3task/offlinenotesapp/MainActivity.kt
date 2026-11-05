package com.web3task.offlinenotesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.web3task.offlinenotesapp.data.local.NotesDatabase
import com.web3task.offlinenotesapp.data.repository.NotesRepository
import com.web3task.offlinenotesapp.navigation.AppNavigation
import com.web3task.offlinenotesapp.ui.home.NotesViewModel
import com.web3task.offlinenotesapp.ui.home.NotesViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = NotesDatabase.getDatabase(applicationContext)
        val repository = NotesRepository(database.noteDao())

        setContent {
            val notesViewModel: NotesViewModel = viewModel(
                factory = NotesViewModelFactory(repository)
            )

            MaterialTheme {
                Surface {
                    AppNavigation(
                        viewModel = notesViewModel,
                        repository = repository
                    )
                }

            }
        }
    }
}
