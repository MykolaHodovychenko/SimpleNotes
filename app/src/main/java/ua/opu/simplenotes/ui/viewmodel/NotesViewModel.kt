package ua.opu.simplenotes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ua.opu.simplenotes.ui.screens.Note
import ua.opu.simplenotes.ui.screens.NoteColor
import ua.opu.simplenotes.ui.screens.NoteEditUiState
import ua.opu.simplenotes.ui.screens.NotesListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.opu.simplenotes.data.repository.NoteRepository

class NotesViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    val notesListUiState: StateFlow<NotesListUiState> =
        repository.getAllNotes()
            .map { notes ->
                NotesListUiState(
                    notes = notes
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = NotesListUiState()
            )

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
        viewModelScope.launch {
            val note = repository.getNoteById(noteId)

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

        viewModelScope.launch {
            val note = Note(
                id = currentState.noteId ?: 0,
                title = currentState.title.trim(),
                text = currentState.text.trim(),
                color = currentState.color
            )

            if (currentState.isEditMode) {
                repository.updateNote(note)
            } else {
                repository.insertNote(note)
            }
        }
    }

    fun deleteNote(noteId: Int) {
        viewModelScope.launch {
            repository.deleteNoteById(noteId)
        }
    }
}