package com.msomu.androidkt.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.msomu.androidkt.model.TodoItem
import com.msomu.androidkt.presentation.ui.components.SingleTodoItem
import com.msomu.androidkt.presentation.viewmodel.HomeUiState
import com.msomu.androidkt.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateTodo: (Int) -> Unit,
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        Box(Modifier.padding(innerPadding)) {
            when (uiState) {
                is HomeUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text((uiState as HomeUiState.Error).message)
                    }
                }

                is HomeUiState.Loading -> {
                    Box(modifier = Modifier.size(120.dp)) {
                        CircularProgressIndicator()
                    }
                }

                is HomeUiState.Success -> {
                    HomeScreen(
                        Modifier.fillMaxSize(),
                        todos = (uiState as HomeUiState.Success).todoItems,
                        onNavigateTodo = onNavigateTodo
                    )
                }
            }
        }
    }
}

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier, todos: List<TodoItem>, onNavigateTodo: (Int) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(todos) {
            SingleTodoItem(
                modifier = modifier, todoItem = it, onNavigateTodo = onNavigateTodo
            )
        }
    }
}