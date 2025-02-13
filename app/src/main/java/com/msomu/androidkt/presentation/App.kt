package com.msomu.androidkt.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.msomu.androidkt.presentation.NavRoutes.DETAIL
import com.msomu.androidkt.presentation.NavRoutes.HOME
import com.msomu.androidkt.presentation.ui.screen.DetailScreen
import com.msomu.androidkt.presentation.ui.screen.HomeScreen

@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = HOME) {
        composable(HOME) {
            HomeScreen(Modifier
                .fillMaxSize()
                .padding(16.dp)) {
                navController.navigate("$DETAIL/$it")
            }
        }
        composable(
            "$DETAIL/{todoItemId}",
            arguments = listOf(navArgument("todoItemId") { type = NavType.IntType })
        ) {
            val todoItemId = it.arguments?.getInt("todoItemId")
            requireNotNull(todoItemId) { "todoItemId parameter not found" }
            DetailScreen(todoItemId, Modifier
                .fillMaxSize()
                .padding(16.dp)){
                navController.popBackStack()
            }
        }
    }
}