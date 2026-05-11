package ua.opu.simplenotes.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ua.opu.simplenotes.data.local.dao.NoteDao
import ua.opu.simplenotes.data.local.entity.NoteEntity
import ua.opu.simplenotes.data.mapper.toEntity
import ua.opu.simplenotes.data.mapper.toNote
import ua.opu.simplenotes.ui.screens.Note

class NoteRepository(
    private val noteDao: NoteDao
) {

    fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { noteEntities ->
            noteEntities.map { noteEntity ->
                noteEntity.toNote()
            }
        }
    }

    suspend fun getNoteById(noteId: Int): Note? {
        return noteDao.getNoteById(noteId)?.toNote()
    }

    suspend fun insertNote(note: Note) {
        noteDao.insertNote(note.toEntityForInsert())
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note.toEntity())
    }

    suspend fun deleteNoteById(noteId: Int) {
        noteDao.deleteNoteById(noteId)
    }

    private fun Note.toEntityForInsert(): NoteEntity {
        return NoteEntity(
            title = title,
            text = text,
            color = color.name
        )
    }
}