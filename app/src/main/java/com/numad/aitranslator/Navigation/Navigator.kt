package com.numad.aitranslator.Navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.numad.aitranslator.screens.HomeScreen

@Composable
fun Navigator(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController, modifier)
        }
    }
}

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Translate : Screen("translate")
}

sealed class ScreenInitParams(val type: String) {
    data object AudioToTranslation : ScreenInitParams("mic")
    data object TextToTranslation : ScreenInitParams("text")
    data object ImageToTranslation : ScreenInitParams("image")
}