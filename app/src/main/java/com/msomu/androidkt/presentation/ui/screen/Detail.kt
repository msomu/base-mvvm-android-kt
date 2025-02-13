package com.msomu.androidkt.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.msomu.androidkt.model.TodoItem
import com.msomu.androidkt.presentation.ui.components.ShimmerCircleImage
import com.msomu.androidkt.presentation.viewmodel.DetailUiState
import com.msomu.androidkt.presentation.viewmodel.DetailViewModel

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
        Box(Modifier.padding(innerPadding)) {
            when (uiState) {
                is DetailUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text((uiState as DetailUiState.Error).message)
                    }
                }

                is DetailUiState.Loading -> {
                    Box(modifier = Modifier.size(120.dp)) {
                        CircularProgressIndicator()
                    }
                }

                is DetailUiState.Success -> {
                    DetailScreen(
                        Modifier.fillMaxSize(),
                        todo = (uiState as DetailUiState.Success).todoItem,
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ShimmerCircleImage(
                        imageUrl = todo.userImage ?: "",
                        modifier = Modifier
                            .width(42.dp)
                            .aspectRatio(1f)
                    )
                    Text(
                        text = todo.title,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Checkbox(
                        checked = todo.completed,
                        onCheckedChange = { }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Status chip
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (todo.completed)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Text(
                        text = if (todo.completed) "Completed" else "Active",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = if (todo.completed)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Description Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "No description provided",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Metadata Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Details",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetadataItem(
                        icon = Icons.Default.DateRange,
                        label = "Created",
                        value = "13/02/2025"
                    )
                    MetadataItem(
                        icon = Icons.Default.Notifications,
                        label = "Due Date",
                        value = "16/04/2026"
                    )
                }
            }
        }
    }
}

@Composable
private fun MetadataItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}