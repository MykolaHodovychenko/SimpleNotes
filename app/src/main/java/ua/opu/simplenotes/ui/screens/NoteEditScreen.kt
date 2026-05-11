package ua.opu.simplenotes.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class NoteEditUiState(
    val noteId: Int? = null,
    val title: String = "",
    val text: String = "",
    val color: NoteColor = NoteColor.Yellow,
    val isEditMode: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditScreen(
    uiState: NoteEditUiState,
    onTitleChange: (String) -> Unit,
    onTextChange: (String) -> Unit,
    onColorChange: (NoteColor) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.isEditMode) {
                            "Редагування нотатки"
                        } else {
                            "Нова нотатка"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Повернутися назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = uiState.color.toBackgroundColor()
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Дані нотатки",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = uiState.title,
                        onValueChange = onTitleChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(text = "Заголовок")
                        },
                        placeholder = {
                            Text(text = "Введіть заголовок нотатки")
                        },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = uiState.text,
                        onValueChange = onTextChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        label = {
                            Text(text = "Текст нотатки")
                        },
                        placeholder = {
                            Text(text = "Введіть текст нотатки")
                        },
                        maxLines = 8
                    )
                }
            }

            Text(
                text = "Колір нотатки",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            ColorSelector(
                selectedColor = uiState.color,
                onColorChange = onColorChange
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 14.dp),
                enabled = uiState.title.isNotBlank() || uiState.text.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = if (uiState.isEditMode) {
                        "Зберегти зміни"
                    } else {
                        "Створити нотатку"
                    }
                )
            }
        }
    }
}

@Composable
private fun ColorSelector(
    selectedColor: NoteColor,
    onColorChange: (NoteColor) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ColorOptionChip(
                modifier = Modifier.weight(1f),
                color = NoteColor.Yellow,
                selected = selectedColor == NoteColor.Yellow,
                onClick = {
                    onColorChange(NoteColor.Yellow)
                }
            )

            ColorOptionChip(
                modifier = Modifier.weight(1f),
                color = NoteColor.Green,
                selected = selectedColor == NoteColor.Green,
                onClick = {
                    onColorChange(NoteColor.Green)
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ColorOptionChip(
                modifier = Modifier.weight(1f),
                color = NoteColor.Blue,
                selected = selectedColor == NoteColor.Blue,
                onClick = {
                    onColorChange(NoteColor.Blue)
                }
            )

            ColorOptionChip(
                modifier = Modifier.weight(1f),
                color = NoteColor.Pink,
                selected = selectedColor == NoteColor.Pink,
                onClick = {
                    onColorChange(NoteColor.Pink)
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ColorOptionChip(
                modifier = Modifier.weight(1f),
                color = NoteColor.Purple,
                selected = selectedColor == NoteColor.Purple,
                onClick = {
                    onColorChange(NoteColor.Purple)
                }
            )

            ColorOptionChip(
                modifier = Modifier.weight(1f),
                color = NoteColor.Orange,
                selected = selectedColor == NoteColor.Orange,
                onClick = {
                    onColorChange(NoteColor.Orange)
                }
            )
        }
    }
}

@Composable
private fun ColorOptionChip(
    modifier: Modifier = Modifier,
    color: NoteColor,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(14.dp)
                        .height(14.dp)
                        .background(
                            color = color.toMarkerColor(),
                            shape = CircleShape
                        )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(text = color.toShortDisplayName())
            }
        },
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Вибраний колір"
                )
            }
        } else {
            null
        }
    )
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

private fun NoteColor.toShortDisplayName(): String {
    return when (this) {
        NoteColor.Yellow -> "Жовтий"
        NoteColor.Green -> "Зелений"
        NoteColor.Blue -> "Синій"
        NoteColor.Pink -> "Рожевий"
        NoteColor.Purple -> "Фіолетовий"
        NoteColor.Orange -> "Помаранчевий"
    }
}