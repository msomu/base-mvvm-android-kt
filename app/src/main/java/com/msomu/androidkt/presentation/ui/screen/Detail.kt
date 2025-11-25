package com.msomu.androidkt.presentation.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.msomu.androidkt.model.TodoItem
import com.msomu.androidkt.presentation.ui.animation.CardAnimation
import com.msomu.androidkt.presentation.ui.animation.AnimationSpecs
import com.msomu.androidkt.presentation.ui.components.ShimmerCircleImage
import com.msomu.androidkt.presentation.viewmodel.DetailUiState
import com.msomu.androidkt.presentation.viewmodel.DetailViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    todoItemId: Int,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {
    LaunchedEffect(todoItemId) {
        viewModel.fetchTodo(todoItemId)
    }
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Todo Details") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.Close, "Back")
                }
            },
            actions = {
                IconButton(onClick = { /* Handle edit */ }) {
                    Icon(Icons.Default.Edit, "Edit Todo")
                }
            }
        )
    }, modifier = modifier.fillMaxSize()) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        Crossfade(
            targetState = uiState,
            animationSpec = tween(AnimationSpecs.Duration.MEDIUM),
            modifier = Modifier.padding(innerPadding),
            label = "detail_state_transition"
        ) { state ->
            when (state) {
                is DetailUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            state.message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                is DetailUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                is DetailUiState.Success -> {
                    DetailScreen(
                        Modifier.fillMaxSize(),
                        todo = state.todoItem,
                    )
                }
            }
        }
    }
}

@Composable
internal fun DetailScreen(
    modifier: Modifier = Modifier,
    todo: TodoItem
) {
    var card1Visible by remember { mutableStateOf(false) }
    var card2Visible by remember { mutableStateOf(false) }
    var card3Visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        card1Visible = true
        delay(CardAnimation.getStaggeredDelay(1).toLong())
        card2Visible = true
        delay(CardAnimation.getStaggeredDelay(1).toLong())
        card3Visible = true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        AnimatedVisibility(
            visible = card1Visible,
            enter = CardAnimation.enterAnimation
        ) {
            TodoHeaderCard(todo)
        }

        AnimatedVisibility(
            visible = card2Visible,
            enter = CardAnimation.enterAnimation
        ) {
            DescriptionCard()
        }

        AnimatedVisibility(
            visible = card3Visible,
            enter = CardAnimation.enterAnimation
        ) {
            MetadataCard()
        }
    }
}

@Composable
private fun TodoHeaderCard(todo: TodoItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            TodoHeaderContent(todo)
            Spacer(modifier = Modifier.height(12.dp))
            StatusChip(todo.completed)
        }
    }
}

@Composable
private fun TodoHeaderContent(todo: TodoItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ShimmerCircleImage(
            imageUrl = todo.userImage ?: "",
            modifier = Modifier
                .size(56.dp)
        )
        Text(
            text = todo.title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.weight(1f)
        )
        Checkbox(
            checked = todo.completed,
            onCheckedChange = { }
        )
    }
}

@Composable
private fun StatusChip(completed: Boolean) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (completed)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier.wrapContentWidth()
    ) {
        Text(
            text = if (completed) "Completed" else "Active",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelLarge,
            color = if (completed)
                MaterialTheme.colorScheme.onPrimaryContainer
            else
                MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
private fun DescriptionCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Description",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "No description provided",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun MetadataCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Details",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MetadataItem(
                    icon = Icons.Default.DateRange,
                    label = "Created",
                    value = "13/02/2025",
                    modifier = Modifier.weight(1f)
                )
                MetadataItem(
                    icon = Icons.Default.Notifications,
                    label = "Due Date",
                    value = "16/04/2026",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MetadataItem(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            modifier = Modifier.size(40.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}