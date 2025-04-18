package com.numad.aitranslator.screens

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.shadow
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
import com.numad.aitranslator.dao.GenericResponse
import com.numad.aitranslator.dao.TranslationResults
import com.numad.aitranslator.navigation.Screen
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
    selectedText: String = "",
    existingTranslationId: Long? = null
) {
    val context = LocalContext.current
    val viewModel: TranslatorViewModel =
        (context as ComponentActivity).viewModels<TranslatorViewModel>().value
    var inputText by rememberSaveable { mutableStateOf("") }
    val allowDetection: MutableState<Boolean> = remember { mutableStateOf(true) }
    val allowTranslation: MutableState<Boolean> = remember { mutableStateOf(true) }
    val toastState = rememberToastState()
    val translatedText by viewModel.translatedText.collectAsState()
    val languageFrom by viewModel.languageFrom.collectAsState()
    val languageTo by viewModel.languageTo.collectAsState()
    val triggerError by viewModel.triggerErrorAlert.collectAsState()
    var shouldPullExistingTranslation by rememberSaveable { mutableStateOf(true) }
    var isInitialRender by rememberSaveable { mutableStateOf(true) }
    val existingTransId = rememberSaveable { mutableStateOf(existingTranslationId) }

    BackHandler {
        inputText = ""
        shouldPullExistingTranslation = false
        allowDetection.value = false
        allowTranslation.value = false
        isInitialRender = true
        existingTransId.value = null
        onBack(
            navController = navController,
            viewModel = viewModel
        )
    }

    LaunchedEffect(Unit) {
        if (selectedText.isNotEmpty()) {
            inputText = selectedText
            shouldPullExistingTranslation = false
            allowDetection.value = true
            allowTranslation.value = true
            isInitialRender = false
        }
    }

    LaunchedEffect(existingTransId, languageTo, languageFrom, inputText) {
        if (existingTransId.value != null && shouldPullExistingTranslation) {
            viewModel.fetchExistingTranslationObject(existingTransId.value!!) {
                inputText = it
                shouldPullExistingTranslation = false
                isInitialRender = false
            }
        } else if (languageFrom != null && languageTo != null && inputText.isNotEmpty() &&
            allowTranslation.value && translatedText.isEmpty()
        ) {
            viewModel.translateText(inputText) { result ->
                if (isInitialRender) {
                    isInitialRender = false
                    return@translateText
                }

                if (result is TranslationResults.Success) {
                    viewModel.saveTranslation(
                        inputText,
                        existingTransId.value,
                        onCompletion = { response ->
                            when (response) {
                                is GenericResponse.Success -> {
                                    existingTransId.value = response.data as? Long
                                    toastState.show(
                                        message = context.getString(R.string.translation_saved),
                                        type = ToastType.SUCCESS,
                                        durationMillis = 3000,
                                        onDismiss = {}
                                    )
                                }

                                is GenericResponse.Failure -> {
                                    toastState.show(
                                        message = context.getString(R.string.something_went_wrong),
                                        type = ToastType.ERROR,
                                        durationMillis = 3000,
                                        onDismiss = { }
                                    )
                                }
                            }
                        }
                    )
                }
            }
        } else if (languageFrom == null) {
            if (inputText.isNotEmpty() && inputText.length >= 5) {
                viewModel.detectLanguage(inputText)
                allowDetection.value = false
            } else {
                allowDetection.value = true
            }
            isInitialRender = false
        }
    }

    if (triggerError) {
        toastState.show(
            message = context.getString(R.string.something_went_wrong),
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
            Header()
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
                        modifier = Modifier
                            .height(24.dp)
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
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    ClickableImage(
                        modifier = Modifier
                            .shadow(elevation = 16.dp, shape = CircleShape)
                            .background(color = White, shape = CircleShape)
                            .border(width = 2.dp, color = Black, shape = CircleShape),
                        imageId = R.drawable.save,
                        descriptionId = R.string.save_description,
                    ) {
                        if (inputText.isNotEmpty() && languageFrom != null) {
                            viewModel.saveTranslation(
                                inputText,
                                existingTransId.value
                            ) { response ->
                                when (response) {
                                    is GenericResponse.Success -> {
                                        existingTransId.value = response.data as? Long
                                        toastState.show(
                                            message = context.getString(R.string.translation_saved),
                                            type = ToastType.SUCCESS,
                                            durationMillis = 3000,
                                            onDismiss = {
                                                inputText = ""
                                                shouldPullExistingTranslation = false
                                                allowDetection.value = false
                                                allowTranslation.value = false
                                                existingTransId.value = null
                                                isInitialRender = true
                                                onBack(
                                                    navController = navController,
                                                    viewModel = viewModel
                                                )
                                            }
                                        )
                                    }

                                    is GenericResponse.Failure -> {
                                        toastState.show(
                                            message = context.getString(R.string.something_went_wrong),
                                            type = ToastType.ERROR,
                                            durationMillis = 3000,
                                            onDismiss = { }
                                        )
                                    }
                                }
                            }
                        } else {
                            toastState.show(
                                message = context.getString(R.string.fill_up_data_to_save),
                                type = ToastType.WARNING,
                                durationMillis = 3000,
                                onDismiss = { }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    ClickableImage(
                        modifier = Modifier
                            .shadow(elevation = 16.dp, shape = CircleShape)
                            .background(color = White, shape = CircleShape)
                            .border(width = 2.dp, color = Black, shape = CircleShape),
                        imageId = R.drawable.translate,
                        descriptionId = R.string.translate_description,
                    ) {
                        if (inputText.isNotEmpty() && languageFrom != null && languageTo != null) {
                            viewModel.translateText(
                                inputText,
                                onCompletion = { result ->
                                    if (result is TranslationResults.Success) {
                                        viewModel.saveTranslation(
                                            inputText,
                                            existingTransId.value,
                                            onCompletion = { response ->
                                                when (response) {
                                                    is GenericResponse.Success -> {
                                                        existingTransId.value =
                                                            response.data as? Long
                                                        toastState.show(
                                                            message = context.getString(R.string.translation_saved),
                                                            type = ToastType.SUCCESS,
                                                            durationMillis = 3000,
                                                            onDismiss = {}
                                                        )
                                                    }

                                                    is GenericResponse.Failure -> {
                                                        toastState.show(
                                                            message = context.getString(R.string.something_went_wrong),
                                                            type = ToastType.ERROR,
                                                            durationMillis = 3000,
                                                            onDismiss = { }
                                                        )
                                                    }
                                                }
                                            }
                                        )
                                    }
                                }
                            )
                        } else {
                            toastState.show(
                                message = context.getString(R.string.fill_up_data_to_translate),
                                type = ToastType.WARNING,
                                durationMillis = 3000,
                                onDismiss = { }
                            )
                        }
                    }
                }
            }
        }
        ToastComponent(toastState)
    }
}

fun onBack(
    navController: NavController,
    viewModel: TranslatorViewModel
) {
    viewModel.reset()
    navController.popBackStack()
}

@Preview(name = "Translator Screen Preview", showBackground = true, showSystemUi = true)
@Composable
private fun TranslatorScreenPreview() {
    TranslatorScreen(
        navController = NavController(LocalContext.current),
        modifier = Modifier
            .statusBarsPadding()
            .safeDrawingPadding(),
    )
}
