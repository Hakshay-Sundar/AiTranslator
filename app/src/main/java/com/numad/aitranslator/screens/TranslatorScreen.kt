package com.numad.aitranslator.screens

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.numad.aitranslator.R
import com.numad.aitranslator.components.ArrowComponent
import com.numad.aitranslator.components.ClickableImage
import com.numad.aitranslator.components.Header
import com.numad.aitranslator.components.LanguageHolder
import com.numad.aitranslator.components.ToastComponent
import com.numad.aitranslator.components.ToastType
import com.numad.aitranslator.components.rememberToastState
import com.numad.aitranslator.navigation.Screen
import com.numad.aitranslator.navigation.TranslateScreenParams
import com.numad.aitranslator.ui.theme.Black
import com.numad.aitranslator.ui.theme.Typography
import com.numad.aitranslator.ui.theme.White
import com.numad.aitranslator.utils.LanguageUtils
import com.numad.aitranslator.viewmodels.TranslatorViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun TranslatorScreen(
    navController: NavController,
    modifier: Modifier,
    type: String = TranslateScreenParams.TEXT_TO_TRANSLATION
) {
    val viewModel: TranslatorViewModel =
        (LocalContext.current as ComponentActivity).viewModels<TranslatorViewModel>().value
    var inputText by rememberSaveable { mutableStateOf("") }
    val allowDetection: MutableState<Boolean> = remember { mutableStateOf(true) }
    val toastState = rememberToastState()
    val translatedText by viewModel.translatedText.collectAsState()
    val languageFrom by viewModel.languageFrom.collectAsState()
    val languageTo by viewModel.languageTo.collectAsState()
    val triggerError by viewModel.triggerErrorAlert.collectAsState()

    BackHandler {
        viewModel.reset()
        navController.popBackStack()
    }

    LaunchedEffect(languageTo, languageFrom, inputText) {
        if (languageFrom != null && languageTo != null && inputText.isNotEmpty()) {
            viewModel.translateText(inputText)
        } else if (languageFrom == null) {
            if (inputText.isNotEmpty() && inputText.length >= 5) {
                viewModel.detectLanguage(inputText)
                allowDetection.value = false
            } else {
                allowDetection.value = true
            }
        }
    }

    if (triggerError) {
        toastState.show(
            message = LocalContext.current.getString(R.string.something_went_wrong),
            type = ToastType.ERROR,
            durationMillis = 3000,
            onDismiss = {
                viewModel.resetErrorAlert()
            }
        )
    }

    Box {
        Column(
            modifier = modifier.fillMaxSize()
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
                        viewModel.reset()
                        navController.popBackStack()
                    }
                }
                Box(
                    modifier = Modifier.weight(0.8f)
                ) {
                    Header()
                }
            }
            Box(
                modifier = Modifier.weight(1f)
            ) {
                Column {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Absolute.SpaceEvenly
                    ) {
                        LanguageHolder(
                            language = languageFrom,
                            onClick = {
                                allowDetection.value = false
                                navController.navigate(
                                    route = Screen.SelectLanguage.createRoute(detectionType = LanguageUtils.DETECTION_DICTIONARY)
                                )
                            }
                        )
                        ArrowComponent(
                            modifier = Modifier
                                .size(width = 36.dp, height = 24.dp)
                                .align(Alignment.CenterVertically)
                        )
                        LanguageHolder(
                            language = languageTo,
                            onClick = {
                                navController.navigate(
                                    route = Screen.SelectLanguage.createRoute(detectionType = LanguageUtils.TRANSLATION_DICTIONARY)
                                )
                            }
                        )
                    }
                    Spacer(
                        modifier = Modifier.height(
                            if (
                                TranslateScreenParams.AUDIO_TO_TRANSLATION.equals(
                                    type, ignoreCase = true
                                )
                            ) 12.dp
                            else 24.dp
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Absolute.SpaceEvenly
                    ) {
                        TextField(
                            value = inputText,
                            onValueChange = {
                                inputText = it
                                if (viewModel.languageFrom.value == null &&
                                    allowDetection.value &&
                                    it.isNotEmpty() &&
                                    it.length > 5
                                ) {
                                    viewModel.detectLanguage(it)
                                    allowDetection.value = false
                                }
                                if (languageFrom != null && languageTo != null) {
                                    viewModel.translateText(it)
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .padding(start = 24.dp, bottom = 24.dp, end = 6.dp)
                                .align(Alignment.CenterVertically)
                                .border(
                                    width = 0.2.dp, color = Black, shape = RoundedCornerShape(12.dp)
                                ),
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = White,
                                unfocusedContainerColor = White,
                                disabledContainerColor = White,
                                focusedIndicatorColor = White,
                                unfocusedIndicatorColor = White,
                                cursorColor = Black
                            ),
                            textStyle = Typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        TextField(
                            value = translatedText,
                            enabled = false,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .padding(start = 6.dp, bottom = 24.dp, end = 24.dp)
                                .align(Alignment.CenterVertically)
                                .border(
                                    width = 0.2.dp, color = Black, shape = RoundedCornerShape(12.dp)
                                ),
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = White,
                                unfocusedContainerColor = White,
                                disabledContainerColor = White,
                                focusedIndicatorColor = White,
                                unfocusedIndicatorColor = White,
                                disabledIndicatorColor = White
                            ),
                            textStyle = Typography.bodyLarge
                        )
                    }
                }
                if (
                    TranslateScreenParams.AUDIO_TO_TRANSLATION.equals(
                        type, ignoreCase = true
                    )
                ) {
                    ClickableImage(
                        modifier = Modifier
                            .background(color = White, shape = CircleShape)
                            .border(width = 2.dp, color = Black, shape = CircleShape)
                            .align(Alignment.BottomCenter),
                        imageId = R.drawable.mic,
                        descriptionId = R.string.mic_description,
                    ) { }
                }
            }
        }
        ToastComponent(toastState)
    }
}

@Preview(name = "Translator Screen Preview", showBackground = true, showSystemUi = true)
@Composable
private fun TranslatorScreenPreview() {
    TranslatorScreen(
        navController = NavController(LocalContext.current),
        modifier = Modifier
            .statusBarsPadding()
            .safeDrawingPadding(),
        type = TranslateScreenParams.AUDIO_TO_TRANSLATION,
    )
}
