package com.msomu.androidkt.model

import kotlinx.serialization.Serializable

@Serializable
data class TodoItem(
    val completed: Boolean,
    val id: Int,
    val title: String,
    val userId: Int
)