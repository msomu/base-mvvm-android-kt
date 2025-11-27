package com.msomu.androidkt.presentation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes for Navigation 3.
 * Each route implements NavKey to enable persistent back stack functionality.
 */
sealed interface AppRoute : NavKey {
    @Serializable
    data object Home : AppRoute

    @Serializable
    data class Detail(val todoItemId: Int) : AppRoute
}
