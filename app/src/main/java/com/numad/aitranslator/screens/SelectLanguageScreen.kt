package com.numad.aitranslator.screens

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.numad.aitranslator.R
import com.numad.aitranslator.components.ClickableImage
import com.numad.aitranslator.components.Header
import com.numad.aitranslator.ui.theme.DividerGray
import com.numad.aitranslator.ui.theme.White
import com.numad.aitranslator.viewmodels.TranslatorViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun SelectLanguageScreen(
    navController: NavController,
    modifier: Modifier,
    detectionType: Int
) {
    val viewModel: TranslatorViewModel =
        (LocalContext.current as ComponentActivity).viewModels<TranslatorViewModel>().value

    LaunchedEffect(Unit) {
        viewModel.getLanguagesBasedOnDictionary(detectionType)
    }
    val languages by viewModel.languagesFromDetectionDictionary.collectAsState()

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
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .background(color = White, shape = RoundedCornerShape(12.dp)),
        ) {
            item {
                LanguageItem(
                    language = LocalContext.current.getString(R.string.select_language_description),
                    onClick = { _ ->
                        viewModel.setLanguageSelected("", detectionType)
                        navController.popBackStack()
                    }
                )
            }
            items(languages) { language ->
                LanguageItem(
                    language = language,
                    onClick = { lang ->
                        viewModel.setLanguageSelected(lang, detectionType)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
fun LanguageItem(
    modifier: Modifier = Modifier,
    language: String,
    onClick: (language: String) -> Unit
) {
    Column(
        modifier = modifier
            .padding(top = 12.dp)
            .clickable {
                onClick(language)
            },
    ) {
        Text(
            text = language,
            modifier = modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = DividerGray,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .align(Alignment.CenterHorizontally)
        )
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
        detectionType = 1
    )
}