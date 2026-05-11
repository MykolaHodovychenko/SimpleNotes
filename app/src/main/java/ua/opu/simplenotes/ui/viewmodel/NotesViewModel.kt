package ua.opu.simplenotes.ui.viewmodel

import androidx.lifecycle.ViewModel
import ua.opu.simplenotes.ui.screens.Note
import ua.opu.simplenotes.ui.screens.NoteColor
import ua.opu.simplenotes.ui.screens.NoteEditUiState
import ua.opu.simplenotes.ui.screens.NotesListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ua.opu.simplenotes.data.repository.NoteRepository

class NotesViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _notes = MutableStateFlow(
        listOf(
            Note(
                id = 1,
                title = "Купити продукти",
                text = "Молоко, хліб, сир, яйця",
                color = NoteColor.Yellow
            ),
            Note(
                id = 2,
                title = "Ідеї для проєкту",
                text = "Додати екран списку, екран редагування та вибір кольору нотатки",
                color = NoteColor.Blue
            ),
            Note(
                id = 3,
                title = "Плани на вихідні",
                text = "Прибрати квартиру, погуляти, подивитися фільм",
                color = NoteColor.Green
            )
        )
    )

    private val _notesListUiState = MutableStateFlow(
        NotesListUiState(
            notes = _notes.value
        )
    )
    val notesListUiState: StateFlow<NotesListUiState> =
        _notesListUiState.asStateFlow()

    private val _noteEditUiState = MutableStateFlow(
        NoteEditUiState()
    )
    val noteEditUiState: StateFlow<NoteEditUiState> =
        _noteEditUiState.asStateFlow()

    fun prepareNewNote() {
        _noteEditUiState.value = NoteEditUiState(
            noteId = null,
            title = "",
            text = "",
            color = NoteColor.Yellow,
            isEditMode = false
        )
    }

    fun prepareEditNote(noteId: Int) {
        val note = _notes.value.firstOrNull { it.id == noteId }

        if (note != null) {
            _noteEditUiState.value = NoteEditUiState(
                noteId = note.id,
                title = note.title,
                text = note.text,
                color = note.color,
                isEditMode = true
            )
        }
    }

    fun updateTitle(title: String) {
        _noteEditUiState.update { currentState ->
            currentState.copy(
                title = title
            )
        }
    }

    fun updateText(text: String) {
        _noteEditUiState.update { currentState ->
            currentState.copy(
                text = text
            )
        }
    }

    fun updateColor(color: NoteColor) {
        _noteEditUiState.update { currentState ->
            currentState.copy(
                color = color
            )
        }
    }

    fun saveNote() {
        val currentState = _noteEditUiState.value

        if (currentState.title.isBlank() && currentState.text.isBlank()) {
            return
        }

        if (currentState.isEditMode) {
            updateExistingNote(currentState)
        } else {
            createNewNote(currentState)
        }

        refreshListState()
    }

    fun deleteNote(noteId: Int) {
        _notes.update { currentNotes ->
            currentNotes.filterNot { note ->
                note.id == noteId
            }
        }

        refreshListState()
    }

    private fun createNewNote(state: NoteEditUiState) {
        val newNote = Note(
            id = generateNextId(),
            title = state.title.trim(),
            text = state.text.trim(),
            color = state.color
        )

        _notes.update { currentNotes ->
            currentNotes + newNote
        }
    }

    private fun updateExistingNote(state: NoteEditUiState) {
        val noteId = state.noteId ?: return

        _notes.update { currentNotes ->
            currentNotes.map { note ->
                if (note.id == noteId) {
                    note.copy(
                        title = state.title.trim(),
                        text = state.text.trim(),
                        color = state.color
                    )
                } else {
                    note
                }
            }
        }
    }

    private fun refreshListState() {
        _notesListUiState.value = NotesListUiState(
            notes = _notes.value
        )
    }

    private fun generateNextId(): Int {
        return (_notes.value.maxOfOrNull { note -> note.id } ?: 0) + 1
    }
}