package com.numad.aitranslator.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.numad.aitranslator.screens.HomeScreen
import com.numad.aitranslator.screens.TranslatorScreen

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
        composable(
            route = Screen.Translate.route,
            arguments = listOf(navArgument(Screen.Translate.TYPE_ARG) {
                type = NavType.StringType
                defaultValue = TranslateScreenParams.TEXT_TO_TRANSLATION
            })
        ) {
            val type = it.arguments?.getString(Screen.Translate.TYPE_ARG) ?: TranslateScreenParams.TEXT_TO_TRANSLATION
            TranslatorScreen(navController, modifier, type)
        }
    }
}

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Translate :
        Screen("translate/{type}") {
            const val TYPE_ARG = "type"
            fun createRoute(type: String): String = "translate/${type}"
    }
}

/**
 * Creating a series of data classes to track screen parameters.
 * */
data class TranslateScreenParams(val type: String) {
    companion object {
        const val AUDIO_TO_TRANSLATION = "mic"
        const val TEXT_TO_TRANSLATION = "text"
        const val IMAGE_TO_TRANSLATION = "image"
    }
}