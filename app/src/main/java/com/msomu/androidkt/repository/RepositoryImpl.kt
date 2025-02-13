package com.msomu.androidkt.repository

import com.msomu.androidkt.model.TodoItem
import com.msomu.androidkt.network.ApiService
import com.msomu.androidkt.network.ApiState
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : Repository {

    override suspend fun getAllTodos(): ApiState<List<TodoItem>> = try {
        ApiState.Success(apiService.getAllTodoItems())
    } catch (e: Exception) {
        ApiState.Error(errorMsg = e.message.toString())
    }

    override suspend fun getTodoDetails(todoItemId : Int): ApiState<TodoItem> = try {
        ApiState.Success(apiService.getTodoItem(todoItemId))
    } catch (e: Exception) {
        ApiState.Error(errorMsg = e.message.toString())
    }
}