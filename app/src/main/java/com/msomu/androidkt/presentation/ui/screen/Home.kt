package com.msomu.androidkt.presentation.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.msomu.androidkt.model.TodoItem
import com.msomu.androidkt.presentation.ui.animation.ListItemAnimation
import com.msomu.androidkt.presentation.ui.components.SingleTodoItem
import com.msomu.androidkt.presentation.ui.components.SkeletonTodoItem
import com.msomu.androidkt.presentation.viewmodel.HomeUiState
import com.msomu.androidkt.presentation.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateTodo: (Int) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text("Todo List", style = MaterialTheme.typography.headlineMedium) }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        Crossfade(
            targetState = uiState,
            animationSpec = tween(300),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            label = "home_state_transition"
        ) { state ->
            when (state) {
                is HomeUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text(
                            state.message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                is HomeUiState.Loading -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(6) {
                            SkeletonTodoItem()
                        }
                    }
                }

                is HomeUiState.Success -> {
                    HomeScreen(
                        todos = state.todoItems,
                        onNavigateTodo = onNavigateTodo
                    )
                }
            }
        }
    }
}

@Composable
internal fun HomeScreen(
    todos: List<TodoItem>, onNavigateTodo: (Int) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
    ) {
        itemsIndexed(
            items = todos,
            key = { _, todoItem -> todoItem.id }
        ) { index, todoItem ->
            AnimatedVisibility(
                visible = true,
                enter = ListItemAnimation.enterAnimation,
                exit = ListItemAnimation.exitAnimation,
                modifier = Modifier.animateItem()
            ) {
                SingleTodoItem(
                    todoItem = todoItem,
                    onNavigateTodo = onNavigateTodo
                )
            }
        }
    }
}