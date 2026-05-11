package ua.opu.simplenotes.data.mapper

import ua.opu.simplenotes.data.local.entity.NoteEntity
import ua.opu.simplenotes.ui.screens.Note
import ua.opu.simplenotes.ui.screens.NoteColor

fun NoteEntity.toNote(): Note {
    return Note(
        id = id,
        title = title,
        text = text,
        color = color.toNoteColor()
    )
}

fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        text = text,
        color = color.name
    )
}

private fun String.toNoteColor(): NoteColor {
    return try {
        NoteColor.valueOf(this)
    } catch (exception: IllegalArgumentException) {
        NoteColor.Yellow
    }
}