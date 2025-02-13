package com.msomu.androidkt.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .clickable { onNavigateTodo(todoItem.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todoItem.completed,
                onCheckedChange = {},
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = todoItem.title,
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = if (todoItem.completed) TextDecoration.LineThrough else null,
                color = if (todoItem.completed)
                    MaterialTheme.colorScheme.outline
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