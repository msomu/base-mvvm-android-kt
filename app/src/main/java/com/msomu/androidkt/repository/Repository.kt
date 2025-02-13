package com.msomu.androidkt.repository

import com.msomu.androidkt.model.TodoItem
import com.msomu.androidkt.network.ApiState

interface Repository {
    suspend fun getAllTodos() : ApiState<List<TodoItem>>
    suspend fun getTodoDetails(todoItemId : Int) : ApiState<TodoItem>
}