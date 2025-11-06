package com.msomu.androidkt.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class TodoItem(
    val completed: Boolean,
    val id: Int,
    val title: String,
    val userId: Int,
    val userImage : String? = "https://i.pravatar.cc/300"
)