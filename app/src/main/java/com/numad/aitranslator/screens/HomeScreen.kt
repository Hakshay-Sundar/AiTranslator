package com.numad.aitranslator.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.numad.aitranslator.R
import com.numad.aitranslator.components.Footer
import com.numad.aitranslator.components.Header
import com.numad.aitranslator.navigation.Screen
import com.numad.aitranslator.navigation.TranslateScreenParams
import com.numad.aitranslator.ui.theme.Typography

@Composable
fun HomeScreen(navController: NavController, modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        Header()
        Box(
            modifier = Modifier.weight(1f)
        ) {
            // As of now, there is no persistent data.
            // We will only represent the scenario where there are no
            // translations.
            NoTranslations(
                onClick = {
                    navController.navigate(
                        route = Screen.Translate.createRoute(type = TranslateScreenParams.TEXT_TO_TRANSLATION)
                    )
                }
            )
        }
        Footer(
            modifier = modifier,
            onTabClick = { screenType ->
                Log.d(
                    "Home Screen",
                    "HomeScreen: ${Screen.Translate.createRoute(type = screenType)}"
                )
                navController.navigate(
                    route = Screen.Translate.createRoute(type = screenType)
                )
            }
        )
    }
}

@Composable
fun NoTranslations(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.add_circle),
            contentDescription = stringResource(R.string.create_new_translations_description)
        )
        Text(
            text = stringResource(R.string.create_new_translations_description),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 4.dp),
            style = Typography.bodyMedium
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true, name = "Home Screen")
private fun HomeScreenPreview() {
    HomeScreen(
        navController = NavController(LocalContext.current),
        modifier = Modifier
            .statusBarsPadding()
            .safeDrawingPadding()
    )
}