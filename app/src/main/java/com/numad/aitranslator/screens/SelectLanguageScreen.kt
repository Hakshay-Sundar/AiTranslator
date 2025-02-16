package com.numad.aitranslator.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.numad.aitranslator.R
import com.numad.aitranslator.components.ClickableImage
import com.numad.aitranslator.components.Header
import com.numad.aitranslator.ui.theme.White

@Composable
fun SelectLanguageScreen(
    navController: NavController,
    modifier: Modifier,
    // TODO: Need to figure out a way to send back the selection of language
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .background(color = White)
                .layoutId("header")
        ) {
            Box(
                modifier = Modifier
                    .weight(0.2f)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        navController.popBackStack()
                    }, contentAlignment = Alignment.Center
            ) {
                ClickableImage(
                    imageId = R.drawable.back_arrow,
                    descriptionId = R.string.back_button_description
                ) {
                    navController.popBackStack()
                }
            }
            Box(
                modifier = Modifier.weight(0.8f)
            ) {
                Header()
            }
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .background(color = White, shape = RoundedCornerShape(12.dp))
        ) {
            // TODO: Need to figure out how to inflate a list of languages.
        }
    }
}

@Preview(name = "Select Language Screen Preview", showBackground = true, showSystemUi = true)
@Composable
private fun SelectLanguageScreenPreview() {
    SelectLanguageScreen(
        navController = NavController(LocalContext.current),
        modifier = Modifier
            .statusBarsPadding()
            .safeDrawingPadding(),
    )
}