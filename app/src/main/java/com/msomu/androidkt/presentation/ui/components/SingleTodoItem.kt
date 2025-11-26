package com.msomu.androidkt.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.msomu.androidkt.model.TodoItem
import com.msomu.androidkt.presentation.ui.animation.AnimationSpecs

@Composable
fun SingleTodoItem(
    modifier: Modifier = Modifier,
    todoItem: TodoItem,
    onNavigateTodo: (Int) -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) AnimationSpecs.Scale.PRESSED else AnimationSpecs.Scale.DEFAULT,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "card_scale"
    )

    val textAlpha by animateFloatAsState(
        targetValue = if (todoItem.completed) 0.6f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "text_alpha"
    )

    val checkboxScale by animateFloatAsState(
        targetValue = if (todoItem.completed) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "checkbox_scale"
    )

    val containerColor by animateColorAsState(
        targetValue = if (todoItem.completed)
            MaterialTheme.colorScheme.surfaceContainerLow
        else
            MaterialTheme.colorScheme.surfaceContainerHigh,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "container_color"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onNavigateTodo(todoItem.id) }
                )
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (todoItem.completed) 2.dp else 6.dp,
            pressedElevation = if (todoItem.completed) 4.dp else 8.dp
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Checkbox(
                checked = todoItem.completed,
                onCheckedChange = {},
                modifier = Modifier.scale(checkboxScale),
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.outline,
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary
                )
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .graphicsLayer { alpha = textAlpha },
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = todoItem.title,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (todoItem.completed) TextDecoration.LineThrough else null,
                    color = if (todoItem.completed)
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    else
                        MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "User #${todoItem.userId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
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