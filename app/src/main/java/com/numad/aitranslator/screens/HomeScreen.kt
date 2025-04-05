package com.numad.aitranslator.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.numad.aitranslator.R
import com.numad.aitranslator.components.Footer
import com.numad.aitranslator.components.Header
import com.numad.aitranslator.components.SnackbarButton
import com.numad.aitranslator.components.SnackbarComponent
import com.numad.aitranslator.components.ToastComponent
import com.numad.aitranslator.components.ToastType
import com.numad.aitranslator.components.rememberToastState
import com.numad.aitranslator.dao.GenericResponse
import com.numad.aitranslator.navigation.Screen
import com.numad.aitranslator.navigation.TranslateScreenParams
import com.numad.aitranslator.room.entities.TranslationEntity
import com.numad.aitranslator.ui.theme.Black
import com.numad.aitranslator.ui.theme.Typography
import com.numad.aitranslator.ui.theme.White
import com.numad.aitranslator.utils.CAMERA_ID
import com.numad.aitranslator.utils.GALLERY_ID
import com.numad.aitranslator.viewmodels.HomeScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier,
    viewmodel: HomeScreenViewModel = hiltViewModel()
) {
    val translations by viewmodel.translations.collectAsState()
    val context = LocalContext.current
    val toastState = rememberToastState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewmodel.fetchTranslations()
    }

    Scaffold(snackbarHost = {
        SnackbarComponent(
            hostState = snackbarHostState,
            buttons = listOf(
                SnackbarButton(imageId = R.drawable.camera,
                    description = LocalContext.current.getString(R.string.camera_desc),
                    onClick = { _ ->
                        snackbarHostState.currentSnackbarData?.dismiss()
                        navController.navigate(
                            route = Screen.TextSelection.createRoute(
                                imageSourceId = CAMERA_ID
                            )
                        )
                    }), SnackbarButton(imageId = R.drawable.gallery,
                    description = LocalContext.current.getString(R.string.gallery_desc),
                    onClick = { _ ->
                        snackbarHostState.currentSnackbarData?.dismiss()
                        navController.navigate(
                            route = Screen.TextSelection.createRoute(
                                imageSourceId = GALLERY_ID
                            )
                        )
                    })
            )
        )
    }) { paddingValues ->
        Column(
            modifier = modifier.padding(paddingValues)
        ) {
            Header()
            Box(
                modifier = Modifier.weight(1f)
            ) {
                this@Column.AnimatedVisibility(translations.isEmpty()) {
                    NoTranslations(onClick = {
                        navController.navigate(
                            route = Screen.Translate.createRoute()
                        )
                    })
                }
                this@Column.AnimatedVisibility(translations.isNotEmpty()) {
                    Translations(context = context, translations = translations, onClick = { id ->
                        navController.navigate(
                            route = Screen.Translate.createRoute(
                                existingTranslationId = id
                            )
                        )
                    }, onLongClick = { id ->
                        viewmodel.deleteTranslation(id, onCompletion = { it ->
                            when (it) {
                                is GenericResponse.Success -> {
                                    toastState.show(
                                        context.getString(R.string.translation_deleted),
                                        ToastType.SUCCESS,
                                        durationMillis = 2000
                                    )
                                }

                                is GenericResponse.Failure -> {
                                    toastState.show(
                                        context.getString(R.string.translation_deleted_error),
                                        ToastType.SUCCESS
                                    )
                                }
                            }
                        })
                    })
                }
                ToastComponent(toastState = toastState)
            }
            Footer(modifier = modifier, onTabClick = { screenType ->
                if (screenType == TranslateScreenParams.IMAGE_TO_TRANSLATION) {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.choose_image),
                            actionLabel = context.getString(R.string.choose_image),
                            duration = SnackbarDuration.Indefinite
                        )
                    }
                } else {
                    navController.navigate(
                        route = Screen.Translate.createRoute()
                    )
                }
            })
        }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Translations(
    context: Context,
    modifier: Modifier = Modifier,
    translations: List<TranslationEntity>,
    onClick: (Long) -> Unit,
    onLongClick: (Long) -> Unit = {}
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Sets exactly 2 columns
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(translations.size) { item ->
            Box(
                modifier = modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = White)
                    .border(width = 1.dp, color = Black, shape = RoundedCornerShape(12.dp))
                    .combinedClickable(onClick = {
                        onClick(translations[item].id)
                    }, onLongClick = {
                        onLongClick(translations[item].id)
                    })
            ) {
                Text(
                    text = context.getString(
                        R.string.translation_representation, (translations[item].text.substring(
                            0, clampValue(25, 0, translations[item].text.length)
                        ) + "..."), if (translations[item].translatedText.isNotEmpty()) {
                            translations[item].translatedText.substring(
                                0, clampValue(25, 0, translations[item].translatedText.length)
                            ) + "..."
                        } else {
                            context.getString(R.string.unavailable)
                        }
                    ),
                    modifier = modifier
                        .padding(12.dp)
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    style = Typography.bodySmall
                )
            }
        }
    }
}

private fun clampValue(value: Int, minValue: Int, maxValue: Int): Int {
    return when {
        value < minValue -> minValue
        value > maxValue -> maxValue
        else -> value
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