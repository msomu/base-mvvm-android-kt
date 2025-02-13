package com.msomu.androidkt.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msomu.androidkt.model.TodoItem
import com.msomu.androidkt.network.ApiState
import com.msomu.androidkt.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _uiState: MutableStateFlow<HomeUiState> =
        MutableStateFlow(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchAllTodos()
    }

    private fun fetchAllTodos() {
        viewModelScope.launch {
            _uiState.update {
                when (val result = repository.getAllTodos()) {
                    is ApiState.Success -> HomeUiState.Success(result.data)
                    is ApiState.Error -> HomeUiState.Error(result.errorMsg)
                    ApiState.Loading -> HomeUiState.Loading
                }
            }
        }
    }
}

sealed class HomeUiState {
    data class Success(val todoItems: List<TodoItem>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
    data object Loading : HomeUiState()
}