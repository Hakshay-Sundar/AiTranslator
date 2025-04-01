package com.numad.aitranslator.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.numad.aitranslator.screens.HomeScreen
import com.numad.aitranslator.screens.SelectLanguageScreen
import com.numad.aitranslator.screens.TextSelectionScreen
import com.numad.aitranslator.screens.TranslatorScreen
import com.numad.aitranslator.utils.LanguageUtils

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
            arguments = listOf(
                navArgument(Screen.Translate.TYPE_ARG) {
                    type = NavType.StringType
                    defaultValue = TranslateScreenParams.TEXT_TO_TRANSLATION
                },
                navArgument(Screen.Translate.EXISTING_TRANSLATION_ID_ARG) {
                    type = NavType.LongType
                    defaultValue = -1
                }
            )
        ) {
            val type = it.arguments?.getString(Screen.Translate.TYPE_ARG)
                ?: TranslateScreenParams.TEXT_TO_TRANSLATION

            val existingTranslationId =
                it.arguments?.getLong(Screen.Translate.EXISTING_TRANSLATION_ID_ARG)

            TranslatorScreen(
                navController,
                modifier,
                type,
                if (existingTranslationId == -1L) null else existingTranslationId
            )
        }
        composable(
            route = Screen.SelectLanguage.route,
            arguments = listOf(navArgument(Screen.SelectLanguage.DETECTION_TYPE_ARG) {
                type = NavType.IntType
                defaultValue = LanguageUtils.DETECTION_DICTIONARY
            })
        ) {
            val detectionType = it.arguments?.getInt(Screen.SelectLanguage.DETECTION_TYPE_ARG) ?: 0
            SelectLanguageScreen(
                navController,
                modifier,
                detectionType,
            )
        }
        composable(
            route = Screen.TextSelection.route,
            arguments = listOf(navArgument(Screen.TextSelection.IMAGE_SOURCE_ID_ARG) {
                type = NavType.IntType
                defaultValue = -1
            })
        ) {
            val imageSourceId = it.arguments?.getInt(Screen.TextSelection.IMAGE_SOURCE_ID_ARG) ?: -1
            TextSelectionScreen(
                modifier,
                navController,
                imageSourceId
            )
        }
    }
}

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Translate :
        Screen("translate/{type}&{existingTranslationId}") {
        const val TYPE_ARG = "type"
        const val EXISTING_TRANSLATION_ID_ARG = "existingTranslationId"
        fun createRoute(type: String, existingTranslationId: Long = -1L): String =
            "translate/${type}&${existingTranslationId}"
    }

    data object SelectLanguage : Screen("selectLanguage/{detectionType}") {
        const val DETECTION_TYPE_ARG = "detectionType"
        fun createRoute(detectionType: Int): String = "selectLanguage/${detectionType}"
    }

    data object TextSelection : Screen("textSelection/{imageSourceId}") {
        const val IMAGE_SOURCE_ID_ARG = "imageSourceId"
        fun createRoute(imageSourceId: Int): String = "textSelection/${imageSourceId}"
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