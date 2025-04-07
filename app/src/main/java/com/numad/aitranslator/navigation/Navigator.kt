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

/**
 * The navigator is responsible for rendering the UI.
 * It initialises by loading up the HomeScreen.
 * <br><br>
 * We pass the routes to various branches of the navigation graph wrapped in composable objects.
 * */
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
                navArgument(Screen.Translate.SELECTED_TEXT_ARG) {
                    type = NavType.StringType
                    defaultValue = TranslateScreenParams.TEXT_TO_TRANSLATION
                },
                navArgument(Screen.Translate.EXISTING_TRANSLATION_ID_ARG) {
                    type = NavType.LongType
                    defaultValue = -1
                }
            )
        ) {
            val selectedText = it.arguments?.getString(Screen.Translate.SELECTED_TEXT_ARG)
                ?: ""

            val existingTranslationId =
                it.arguments?.getLong(Screen.Translate.EXISTING_TRANSLATION_ID_ARG)

            TranslatorScreen(
                navController,
                modifier,
                selectedText,
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

/**
 * This sealed class tracks all possible screens we have within the app.
 * This application has 4 screens:
 * 1. HomeScreen - Dashboard screen
 * 2. TranslatorScreen - Screen where the user can translate text
 * 3. SelectLanguageScreen - Screen where the user can select a language either for translation or detection
 * 4. TextSelectionScreen - Screen where the user can select texts obtained from reading an image.
 * */
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Translate :
        Screen("translate/{selectedText}&{existingTranslationId}") {
        const val SELECTED_TEXT_ARG = "selectedText"
        const val EXISTING_TRANSLATION_ID_ARG = "existingTranslationId"
        fun createRoute(selectedText: String = "", existingTranslationId: Long = -1L): String =
            "translate/${selectedText}&${existingTranslationId}"
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
        const val TEXT_TO_TRANSLATION = "text"
        const val IMAGE_TO_TRANSLATION = "image"
    }
}