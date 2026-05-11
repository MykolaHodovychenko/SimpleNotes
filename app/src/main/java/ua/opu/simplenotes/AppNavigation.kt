package ua.opu.simplenotes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ua.opu.simplenotes.ui.screens.NoteEditScreen
import ua.opu.simplenotes.ui.screens.NotesListScreen
import ua.opu.simplenotes.ui.viewmodel.NotesViewModel

object AppRoutes {
    const val NOTES_LIST = "notes_list"
    const val NOTE_CREATE = "note_create"
    const val NOTE_EDIT = "note_edit/{noteId}"

    fun noteEdit(noteId: Int): String {
        return "note_edit/$noteId"
    }
}

@Composable
fun AppNavigation(
    notesViewModel: NotesViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.NOTES_LIST
    ) {
        composable(
            route = AppRoutes.NOTES_LIST
        ) {
            val uiState by notesViewModel.notesListUiState.collectAsState()

            NotesListScreen(
                uiState = uiState,
                onAddClick = {
                    notesViewModel.prepareNewNote()
                    navController.navigate(AppRoutes.NOTE_CREATE)
                },
                onNoteClick = { noteId ->
                    navController.navigate(AppRoutes.noteEdit(noteId))
                },
                onDeleteClick = { noteId ->
                    notesViewModel.deleteNote(noteId)
                }
            )
        }

        composable(
            route = AppRoutes.NOTE_CREATE
        ) {
            val uiState by notesViewModel.noteEditUiState.collectAsState()

            NoteEditScreen(
                uiState = uiState,
                onTitleChange = { title ->
                    notesViewModel.updateTitle(title)
                },
                onTextChange = { text ->
                    notesViewModel.updateText(text)
                },
                onColorChange = { color ->
                    notesViewModel.updateColor(color)
                },
                onSaveClick = {
                    notesViewModel.saveNote()
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppRoutes.NOTE_EDIT,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId")

            LaunchedEffect(noteId) {
                if (noteId != null) {
                    notesViewModel.prepareEditNote(noteId)
                }
            }

            val uiState by notesViewModel.noteEditUiState.collectAsState()

            NoteEditScreen(
                uiState = uiState,
                onTitleChange = { title ->
                    notesViewModel.updateTitle(title)
                },
                onTextChange = { text ->
                    notesViewModel.updateText(text)
                },
                onColorChange = { color ->
                    notesViewModel.updateColor(color)
                },
                onSaveClick = {
                    notesViewModel.saveNote()
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}