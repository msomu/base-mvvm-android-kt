package com.msomu.androidkt.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.msomu.androidkt.model.TodoItem

@Composable
fun SingleTodoItem(
    modifier: Modifier = Modifier,
    todoItem: TodoItem,
    onNavigateTodo: (Int) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onNavigateTodo(todoItem.id) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (todoItem.completed) 1.dp else 3.dp
        ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (todoItem.completed)
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Checkbox(
                checked = todoItem.completed,
                onCheckedChange = {},
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.outline
                )
            )

            Text(
                modifier = Modifier.weight(1f),
                text = todoItem.title,
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = if (todoItem.completed) TextDecoration.LineThrough else null,
                color = if (todoItem.completed)
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview
@Composable
private fun SingleTodoItemPreview() {
    Column {
        SingleTodoItem(
            todoItem = TodoItem(
                completed = false,
                id = 9211,
                title = "Wake Up",
                userId = 8612
            )
        ) { }
        SingleTodoItem(
            todoItem = TodoItem(
                completed = true,
                id = 9211,
                title = "Take a Selfie",
                userId = 8616
            )
        ) { }
    }
}