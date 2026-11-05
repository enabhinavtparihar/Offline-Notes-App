package com.web3task.offlinenotesapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.web3task.offlinenotesapp.data.repository.NotesRepository
import com.web3task.offlinenotesapp.uidesgin.editor.NoteEditorScreen
import com.web3task.offlinenotesapp.uidesgin.home.NotesScreen
import com.web3task.offlinenotesapp.ui.home.NotesViewModel

object Routes {
    const val HOME = "home"
    const val EDITOR = "editor/{noteId}"

    fun editor(noteId: Long = 0L) = "editor/$noteId"
}

@Composable
fun AppNavigation(
    viewModel: NotesViewModel,
    repository: NotesRepository
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {

        composable(Routes.HOME) {
            NotesScreen(
                viewModel = viewModel,
                onAddClick = {
                    navController.navigate(Routes.editor())
                },
                onNoteClick = { noteId ->
                    navController.navigate(
                        Routes.editor(noteId)
                    )
                }
            )
        }

        composable(
            route = Routes.EDITOR,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val noteId =
                backStackEntry.arguments?.getLong("noteId") ?: 0L

            NoteEditorScreen(
                noteId = noteId,
                repository = repository,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
