package com.msomu.androidkt.repository

import com.msomu.androidkt.model.TodoItem
import com.msomu.androidkt.network.ApiService
import com.msomu.androidkt.network.ApiState
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : Repository {

    // In-memory cache
    private var cachedTodos: List<TodoItem>? = null
    private var todosLastFetchTime: Long = 0
    private val todoDetailsCache = mutableMapOf<Int, TodoItem>()
    private val detailsLastFetchTime = mutableMapOf<Int, Long>()

    // Cache duration: 5 minutes
    private val CACHE_DURATION_MS = 5 * 60 * 1000L

    override suspend fun getAllTodos(): ApiState<List<TodoItem>> {
        val now = System.currentTimeMillis()

        // Return cached data if still valid
        if (cachedTodos != null && (now - todosLastFetchTime) < CACHE_DURATION_MS) {
            return ApiState.Success(cachedTodos!!)
        }

        return try {
            val data = apiService.getAllTodoItems()
            cachedTodos = data
            todosLastFetchTime = now
            ApiState.Success(data)
        } catch (e: Exception) {
            // Return cached data if available, even if expired
            cachedTodos?.let {
                ApiState.Success(it)
            } ?: ApiState.Error(errorMsg = e.message.toString())
        }
    }

    override suspend fun getTodoDetails(todoItemId: Int): ApiState<TodoItem> {
        val now = System.currentTimeMillis()
        val lastFetch = detailsLastFetchTime[todoItemId] ?: 0

        // Return cached data if still valid
        if (todoDetailsCache.containsKey(todoItemId) && (now - lastFetch) < CACHE_DURATION_MS) {
            return ApiState.Success(todoDetailsCache[todoItemId]!!)
        }

        return try {
            val data = apiService.getTodoItem(todoItemId)
            todoDetailsCache[todoItemId] = data
            detailsLastFetchTime[todoItemId] = now
            ApiState.Success(data)
        } catch (e: Exception) {
            // Return cached data if available, even if expired
            todoDetailsCache[todoItemId]?.let {
                ApiState.Success(it)
            } ?: ApiState.Error(errorMsg = e.message.toString())
        }
    }
}