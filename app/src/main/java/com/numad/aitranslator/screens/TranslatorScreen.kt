package com.numad.aitranslator.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.numad.aitranslator.navigation.TranslateScreenParams
import com.numad.aitranslator.ui.theme.Black
import com.numad.aitranslator.ui.theme.Typography
import com.numad.aitranslator.ui.theme.White


@Composable
fun TranslatorScreen(
    navController: NavController,
    modifier: Modifier,
    type: String = TranslateScreenParams.TEXT_TO_TRANSLATION
) {
    val languageFrom: MutableState<String> = remember { mutableStateOf("English") }
    val languageTo: MutableState<String?> = remember { mutableStateOf(null) }
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
                    LanguageHolder(language = languageFrom.value, onClick = { })
                    ArrowComponent(
                        modifier = Modifier
                            .size(width = 36.dp, height = 24.dp)
                            .align(Alignment.CenterVertically)
                    )
                    LanguageHolder(language = languageTo.value, onClick = { })
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
                        value = "",
                        onValueChange = {},
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
                        textStyle = Typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TextField(
                        value = "",
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
                        textStyle = Typography.bodySmall
                    )
                }
            }
            if(
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
}

@Preview(name = "Translator Screen Preview", showBackground = true, showSystemUi = true)
@Composable
private fun TranslatorScreenPreview() {
    TranslatorScreen(
        navController = NavController(LocalContext.current),
        modifier = Modifier
            .statusBarsPadding()
            .safeDrawingPadding(),
        type = TranslateScreenParams.AUDIO_TO_TRANSLATION
    )
}
