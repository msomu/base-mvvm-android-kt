package com.msomu.androidkt.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.rememberNavBackStack

/**
 * Navigator class that handles navigation events for the app.
 * This class manages the back stack and provides navigation methods.
 */
class AppNavigator(
    val backStack: NavBackStack<AppRoute>
) {
    fun navigateTo(route: AppRoute) {
        backStack.add(route)
    }

    fun goBack(): Boolean {
        return if (backStack.size > 1) {
            backStack.removeLastOrNull()
            true
        } else {
            false
        }
    }
}

@Composable
fun rememberAppNavigator(
    startRoute: AppRoute = AppRoute.Home
): AppNavigator {
    val backStack = rememberNavBackStack(startRoute)
    return remember(backStack) {
        AppNavigator(backStack)
    }
}
