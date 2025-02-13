package com.msomu.androidkt.network
import com.msomu.androidkt.model.TodoItem
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("todos")
    suspend fun getAllTodoItems(): List<TodoItem>

    @GET("todos/{todoItemId}")
    suspend fun getTodoItem(@Path("todoItemId") todoItemId : Int): TodoItem
}