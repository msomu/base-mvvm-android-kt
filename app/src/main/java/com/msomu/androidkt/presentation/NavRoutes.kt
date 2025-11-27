package com.msomu.androidkt.presentation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes for Navigation 3.
 * Using sealed interface ensures exhaustive when expressions.
 */
sealed interface AppRoute {
    @Serializable
    data object Home : AppRoute

    @Serializable
    data class Detail(val todoItemId: Int) : AppRoute
}
