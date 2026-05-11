package ua.opu.simplenotes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

data class Note(
    val id: Int,
    val title: String,
    val text: String,
    val color: NoteColor
)

enum class NoteColor {
    Yellow,
    Green,
    Blue,
    Pink,
    Purple,
    Orange
}

data class NotesListUiState(
    val notes: List<Note> = emptyList()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    uiState: NotesListUiState,
    onAddClick: () -> Unit,
    onNoteClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Нотатки")
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Додати нотатку"
                )
            }
        }
    ) { paddingValues ->
        if (uiState.notes.isEmpty()) {
            EmptyNotesContent(
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = uiState.notes,
                    key = { note -> note.id }
                ) { note ->
                    NoteListItem(
                        note = note,
                        onClick = {
                            onNoteClick(note.id)
                        },
                        onDeleteClick = {
                            onDeleteClick(note.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NoteListItem(
    note: Note,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val backgroundColor = note.color.toBackgroundColor()
    val markerColor = note.color.toMarkerColor()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(72.dp)
                    .background(
                        color = markerColor,
                        shape = RoundedCornerShape(50)
                    )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(10.dp)
                            .height(10.dp)
                            .background(
                                color = markerColor,
                                shape = CircleShape
                            )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = note.text,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = note.color.toDisplayName(),
                    style = MaterialTheme.typography.labelMedium,
                    color = markerColor
                )
            }

            IconButton(
                onClick = onDeleteClick
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Видалити нотатку"
                )
            }
        }
    }
}

@Composable
private fun EmptyNotesContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Нотаток поки немає",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

private fun NoteColor.toBackgroundColor(): Color {
    return when (this) {
        NoteColor.Yellow -> Color(0xFFFFF8D6)
        NoteColor.Green -> Color(0xFFE3F7E8)
        NoteColor.Blue -> Color(0xFFE1F0FF)
        NoteColor.Pink -> Color(0xFFFFE4EF)
        NoteColor.Purple -> Color(0xFFF0E6FF)
        NoteColor.Orange -> Color(0xFFFFE0B2)
    }
}

private fun NoteColor.toMarkerColor(): Color {
    return when (this) {
        NoteColor.Yellow -> Color(0xFFF2C94C)
        NoteColor.Green -> Color(0xFF27AE60)
        NoteColor.Blue -> Color(0xFF2F80ED)
        NoteColor.Pink -> Color(0xFFEB5757)
        NoteColor.Purple -> Color(0xFF9B51E0)
        NoteColor.Orange -> Color(0xFFF2994A)
    }
}

private fun NoteColor.toDisplayName(): String {
    return when (this) {
        NoteColor.Yellow -> "Жовта нотатка"
        NoteColor.Green -> "Зелена нотатка"
        NoteColor.Blue -> "Синя нотатка"
        NoteColor.Pink -> "Рожева нотатка"
        NoteColor.Purple -> "Фіолетова нотатка"
        NoteColor.Orange -> "Помаранчева нотатка"
    }
}