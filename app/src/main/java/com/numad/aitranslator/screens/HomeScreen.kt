package com.numad.aitranslator.screens

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.numad.aitranslator.R
import com.numad.aitranslator.components.ArrowComponent
import com.numad.aitranslator.components.Footer
import com.numad.aitranslator.components.Header
import com.numad.aitranslator.components.ShareCard
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
import com.numad.aitranslator.ui.theme.PastelBlue
import com.numad.aitranslator.ui.theme.PastelRed
import com.numad.aitranslator.ui.theme.Typography
import com.numad.aitranslator.ui.theme.White
import com.numad.aitranslator.ui.theme.getDarkerVariant
import com.numad.aitranslator.ui.theme.getRandomPastelColor
import com.numad.aitranslator.ui.theme.getVariationOfColor
import com.numad.aitranslator.ui.theme.pastelColors
import com.numad.aitranslator.utils.CAMERA_ID
import com.numad.aitranslator.utils.GALLERY_ID
import com.numad.aitranslator.utils.LanguageUtils
import com.numad.aitranslator.utils.shareBitmap
import com.numad.aitranslator.viewmodels.HomeScreenViewModel
import kotlinx.coroutines.delay
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
    val languageColorMap = remember { mutableMapOf<Pair<String, String>, Color>() }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // Bottom Modal for sharing or deleting
    val translationObjectId = remember { mutableStateOf<Long?>(null) }
    val translationObject = remember { mutableStateOf<TranslationEntity?>(null) }
    val showModal = remember { mutableStateOf(false) }

    // Share Alert Dialog
    var showShareDialog by remember { mutableStateOf(false) }

    // Filtering Based on Languages
    val (languageFromFilter, setLanguageFromFilter) = remember { mutableStateOf<String?>(null) }
    val (languageToFilter, setLanguageToFilter) = remember { mutableStateOf<String?>(null) }
    val (searchFilter, setSearchFilter) = remember { mutableStateOf("") }
    val filteredTranslations by remember(
        translations,
        languageFromFilter,
        languageToFilter,
        searchFilter
    ) {
        derivedStateOf {
            translations.let { list ->
                var result = list

                if (languageFromFilter != null && languageToFilter != null) {
                    result = result.filter { translation ->
                        (translation.languageFrom == languageFromFilter) &&
                                (translation.languageTo == languageToFilter)
                    }
                }

                if (searchFilter.isNotEmpty()) {
                    result = result.filter { translation ->
                        translation.text.contains(searchFilter, ignoreCase = true) ||
                                translation.translatedText.contains(searchFilter, ignoreCase = true)
                    }
                }

                result.sortedByDescending { it.timestampMillis }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewmodel.fetchTranslations()
    }

    LaunchedEffect(translations) {
        if (translations.isNotEmpty() && translations.size != languageColorMap.size) {
            val usedColors = mutableSetOf<Color>()

            translations.forEach { translation ->
                val languagePair = Pair(
                    LanguageUtils.getLanguageCode(
                        translation.languageFrom,
                        LanguageUtils.DETECTION_DICTIONARY
                    ),
                    LanguageUtils.getLanguageCode(
                        translation.languageTo,
                        LanguageUtils.TRANSLATION_DICTIONARY
                    )
                )

                if (!languageColorMap.containsKey(languagePair)) {
                    var newColor = getRandomPastelColor()

                    // If all colors are used or we happen to get a very similar color, create variations
                    var attempts = 0
                    while (usedColors.contains(newColor) && attempts < 10) {
                        // If we've used all pastel colors, start creating variations
                        newColor = if (usedColors.size >= pastelColors.size)
                            newColor.getVariationOfColor() else getRandomPastelColor()
                        attempts++
                    }
                    languageColorMap[languagePair] = newColor
                    usedColors.add(newColor)
                }
            }
        }
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
            modifier = modifier
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures {
                        focusManager.clearFocus()
                    }
                }
        ) {
            Header()
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                this@Column.AnimatedVisibility(translations.isEmpty()) {
                    NoTranslations(onClick = {
                        navController.navigate(
                            route = Screen.Translate.createRoute()
                        )
                    })
                }
                this@Column.AnimatedVisibility(translations.isNotEmpty()) {
                    Translations(
                        context = context,
                        translations = filteredTranslations,
                        languageColorMap = languageColorMap,
                        searchFilter = searchFilter,
                        focusRequester = focusRequester,
                        onResetLanguageFilter = {
                            setLanguageFromFilter(null)
                            setLanguageToFilter(null)
                        },
                        onApplyLanguageFilter = { langFromCode, langToCode ->
                            setLanguageFromFilter(langFromCode)
                            setLanguageToFilter(langToCode)
                        },
                        onSearchFilter = { searchFilter ->
                            setSearchFilter(searchFilter)
                        },
                        onClick = { id ->
                            navController.navigate(
                                route = Screen.Translate.createRoute(
                                    existingTranslationId = id
                                )
                            )
                        },
                        onLongClick = { translation ->
                            translationObjectId.value = translation.id
                            translationObject.value = translation
                            showModal.value = true
                        }
                    )
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

        AnimatedVisibility(visible = (showModal.value && translationObjectId.value != null && translationObject.value != null)) {
            TranslationOptionBottomModal(
                context = context,
                onDismissRequest = {
                    showModal.value = false
                    translationObjectId.value = null
                    translationObject.value = null
                },
                onShare = {
                    showModal.value = false
                    showShareDialog = true
//                    translationObjectId.value = null
//                    translationObject.value = null
                },
                onDelete = {
                    showModal.value = false
                    viewmodel.deleteTranslation(
                        translationObjectId.value!!,
                        onCompletion = {
                            translationObjectId.value = null
                            translationObject.value = null
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
                        }
                    )
                }
            )
        }

        AnimatedVisibility(showShareDialog && translationObjectId.value != null && translationObject.value != null) {
            Dialog(
                onDismissRequest = { showShareDialog = false }
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 8.dp,
                    modifier = Modifier.padding(4.dp)
                ) {
                    ShareCard(
                        translation = translationObject.value,
                        pastelColor = getRandomPastelColor(),
                    ) { bitmap: Bitmap ->
                        context.shareBitmap(bitmap)
                        showShareDialog = false
                        scope.launch {
                            delay(1000)
                            translationObjectId.value = null
                            translationObject.value = null
                        }
                    }
                }
            }
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
    languageColorMap: Map<Pair<String, String>, Color>,
    translations: List<TranslationEntity>,
    focusRequester: FocusRequester,
    searchFilter: String? = null,
    onResetLanguageFilter: () -> Unit,
    onApplyLanguageFilter: (String, String) -> Unit,
    onSearchFilter: (String) -> Unit,
    onClick: (Long) -> Unit,
    onLongClick: (TranslationEntity) -> Unit = {}
) {
    Column {
        TextField(
            value = searchFilter ?: "",
            onValueChange = { searchFilter ->
                onSearchFilter(searchFilter)
            },
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .focusRequester(focusRequester)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { /* Do nothing, just consume the event */ })
                },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = stringResource(R.string.search_description)
                )
            },
            trailingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = stringResource(R.string.clear_search_description),
                    modifier = Modifier.clickable {
                        onSearchFilter("")
                    }
                )
            },
            label = {
                Text(text = stringResource(R.string.search_description))
            },
            colors = TextFieldDefaults.colors().copy(
                unfocusedContainerColor = White,
                focusedContainerColor = White,
                unfocusedIndicatorColor = Black,
                focusedIndicatorColor = Black
            )
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = context.getString(R.string.all),
                    modifier = Modifier
                        .height(24.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            color = Black,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                        .clickable(onClick = onResetLanguageFilter),
                    style = Typography.labelSmall,
                    color = White
                )
            }
            items(languageColorMap.keys.toList()) { (langFrom, langTo) ->
                val languageFrom =
                    LanguageUtils.getLanguageName(langFrom, LanguageUtils.DETECTION_DICTIONARY)
                val languageTo =
                    LanguageUtils.getLanguageName(langTo, LanguageUtils.TRANSLATION_DICTIONARY)
                LanguageToLanguage(
                    languageFrom = if (languageFrom == LanguageUtils.UNKNOWN) "" else languageFrom,
                    languageTo = if (languageTo == LanguageUtils.UNKNOWN) "" else languageTo,
                    languageColorMap = languageColorMap,
                    isForTranslation = false,
                    onClickListener = onApplyLanguageFilter
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Sets exactly 2 columns
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 8.dp, top = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(translations.size) { item ->
                Column(
                    modifier = modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(color = White)
                        .border(width = 1.dp, color = Black, shape = RoundedCornerShape(12.dp))
                        .combinedClickable(onClick = {
                            onClick(translations[item].id)
                        }, onLongClick = {
                            onLongClick(translations[item])
                        })
                ) {
                    LanguageToLanguage(
                        languageFrom = translations[item].languageFrom,
                        languageTo = translations[item].languageTo,
                        languageColorMap = languageColorMap
                    )
                    Text(
                        text = context.getString(
                            R.string.translation_representation, (translations[item].text.substring(
                                0, clampValue(10, 0, translations[item].text.length)
                            ) + "..."), if (translations[item].translatedText.isNotEmpty()) {
                                translations[item].translatedText.substring(
                                    0, clampValue(10, 0, translations[item].translatedText.length)
                                ) + "..."
                            } else {
                                context.getString(R.string.unavailable)
                            }
                        ),
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        textAlign = TextAlign.Center,
                        style = Typography.bodySmall,
                    )
                }
            }
        }
    }
}

@Composable
private fun LanguageToLanguage(
    modifier: Modifier = Modifier,
    languageFrom: String,
    languageTo: String,
    languageColorMap: Map<Pair<String, String>, Color>,
    isForTranslation: Boolean = true,
    onClickListener: (String, String) -> Unit = { _, _ -> }
) {
    val langFromCode = LanguageUtils.getLanguageCode(
        languageFrom,
        LanguageUtils.DETECTION_DICTIONARY
    )
    val langToCode = LanguageUtils.getLanguageCode(
        languageTo,
        LanguageUtils.TRANSLATION_DICTIONARY
    )
    val languagePair = Pair(langFromCode, langToCode)
    val badgeColor = languageColorMap[languagePair] ?: PastelBlue

    Box(
        modifier = modifier
            .padding(
                start = if (isForTranslation) 12.dp else 0.dp,
                top = if (isForTranslation) 12.dp else 0.dp
            )
            .height(24.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(badgeColor.copy(alpha = 0.2f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(
                enabled = !isForTranslation,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClickListener(languageFrom, languageTo)
            }
    ) {
        Row {
            val color = badgeColor.getDarkerVariant()
            Text(
                text = languageFrom,
                style = Typography.labelSmall,
                color = color,
                textAlign = TextAlign.Center
            )
            ArrowComponent(
                modifier = Modifier
                    .size(width = 12.dp, height = 16.dp)
                    .padding(2.dp)
                    .align(Alignment.CenterVertically),
                color = color
            )
            Text(
                text = languageTo,
                style = Typography.labelSmall,
                color = color,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TranslationOptionBottomModal(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit,
    context: Context,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp, 16.dp, 32.dp)
        ) {
            Text(
                context.getString(R.string.translation_options),
                style = Typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onShare()
                    }
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = context.getString(R.string.share),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    context.getString(R.string.share),
                    style = Typography.bodyLarge
                )
            }

            HorizontalDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onDelete()
                    }
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = context.getString(R.string.delete),
                    modifier = Modifier.size(24.dp),
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    context.getString(R.string.delete),
                    style = Typography.bodyLarge,
                    color = PastelRed
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