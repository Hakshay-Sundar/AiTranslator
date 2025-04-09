package com.numad.aitranslator

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.numad.aitranslator.navigation.Navigator
import com.numad.aitranslator.ui.theme.AiTranslatorTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main activity sets the theme and calls upon the navigator to render the UI.
 * */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        window.statusBarColor = Color.Black.toArgb()
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
        }
        window.navigationBarColor = Color.Black.toArgb()

        setContent {
            AiTranslatorTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { _ ->
                    Navigator(
                        navController = navController,
                        modifier = Modifier
                            .statusBarsPadding()
                            .safeDrawingPadding()
                    )
                }
            }
        }
    }
}
