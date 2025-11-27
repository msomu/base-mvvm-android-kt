package com.msomu.androidkt.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entry
import androidx.navigation3.ui.NavDisplay
import com.msomu.androidkt.presentation.ui.screen.DetailScreen
import com.msomu.androidkt.presentation.ui.screen.HomeScreen

@Composable
fun App() {
    val navigator = rememberAppNavigator()

    NavDisplay(
        backStack = navigator.backStack,
        onBack = { navigator.goBack() },
        entryProvider = { route ->
            when (route) {
                is AppRoute.Home -> entry(route) {
                    HomeScreen(
                        modifier = Modifier.fillMaxSize(),
                        onNavigateTodo = { todoItemId ->
                            navigator.navigateTo(AppRoute.Detail(todoItemId))
                        }
                    )
                }
                is AppRoute.Detail -> entry(route) {
                    DetailScreen(
                        todoItemId = route.todoItemId,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        onNavigateBack = {
                            navigator.goBack()
                        }
                    )
                }
            }
        }
    )
}
