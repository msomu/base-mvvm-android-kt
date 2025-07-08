package com.msomu.androidkt.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.msomu.androidkt.model.TodoItem

// Animation duration in milliseconds
private const val AnimationDurationMs = 300
// Alpha value for completed tasks
private const val CompletedTaskAlpha = 0.7f

@Composable
fun SingleTodoItem(
    todoItem: TodoItem,
    onNavigateTodo: (Int) -> Unit
) {
    // Animate the card color based on completion status
    val backgroundColor by animateColorAsState(
        targetValue = if (todoItem.completed) 
            MaterialTheme.colorScheme.surfaceVariant
        else 
            MaterialTheme.colorScheme.surface,
        animationSpec = tween(AnimationDurationMs),
        label = "CardBackgroundAnimation"
    )
    
    // Animate the text color based on completion status
    val textColor by animateColorAsState(
        targetValue = if (todoItem.completed)
            MaterialTheme.colorScheme.outline
        else
            MaterialTheme.colorScheme.onSurface,
        animationSpec = tween(AnimationDurationMs),
        label = "TextColorAnimation"
    )
    
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateTodo(todoItem.id) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (todoItem.completed) 1.dp else 3.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                color = textColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.alpha(if (todoItem.completed) CompletedTaskAlpha else 1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SingleTodoItemPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SingleTodoItem(
                todoItem = TodoItem(
                    completed = false,
                    id = 9211,
                    title = "Complete the UI improvements for Todo List screen",
                    userId = 8612
                )
            ) { }
            
            SingleTodoItem(
                todoItem = TodoItem(
                    completed = true,
                    id = 9212,
                    title = "Fix white border/padding issue and improve visual hierarchy",
                    userId = 8616
                )
            ) { }
            
            SingleTodoItem(
                todoItem = TodoItem(
                    completed = false,
                    id = 9213,
                    title = "This is a very long task title that demonstrates text overflow " +
                            "handling with proper ellipsis",
                    userId = 8617
                )
            ) { }
        }
    }
}