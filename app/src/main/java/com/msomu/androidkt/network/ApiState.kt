package com.msomu.androidkt.network

sealed class ApiState<out T> {
    data class Success<out T>(val data: T) : ApiState<T>()

    data class Error(val errorMsg: String) : ApiState<Nothing>()

    data object Loading : ApiState<Nothing>()
}