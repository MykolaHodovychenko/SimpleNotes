package ua.opu.simplenotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import ua.opu.simplenotes.data.local.database.AppDatabase
import ua.opu.simplenotes.data.repository.NoteRepository
import ua.opu.simplenotes.ui.theme.SimpleNotesTheme
import ua.opu.simplenotes.ui.viewmodel.NotesViewModel
import ua.opu.simplenotes.ui.viewmodel.NotesViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var notesViewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val noteDao = database.noteDao()
        val repository = NoteRepository(noteDao)
        val factory = NotesViewModelFactory(repository)

        notesViewModel = ViewModelProvider(
            this,
            factory
        )[NotesViewModel::class.java]

        enableEdgeToEdge()

        setContent {
            SimpleNotesTheme {
                AppNavigation(
                    notesViewModel = notesViewModel
                )
            }
        }
    }
}
