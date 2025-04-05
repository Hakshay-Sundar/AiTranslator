package com.numad.aitranslator.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.numad.aitranslator.R
import com.numad.aitranslator.components.ToastComponent
import com.numad.aitranslator.components.ToastType
import com.numad.aitranslator.components.rememberToastState
import com.numad.aitranslator.navigation.Screen
import com.numad.aitranslator.ui.theme.Black
import com.numad.aitranslator.ui.theme.DividerGray
import com.numad.aitranslator.ui.theme.SelectedBlue
import com.numad.aitranslator.ui.theme.Typography
import com.numad.aitranslator.ui.theme.White
import com.numad.aitranslator.utils.CAMERA_ID
import com.numad.aitranslator.utils.CameraHandler
import com.numad.aitranslator.utils.GALLERY_ID
import com.numad.aitranslator.utils.GalleryImagePicker
import com.numad.aitranslator.viewmodels.TextSelectionViewModel

@SuppressLint("MutableCollectionMutableState")
@Composable
fun TextSelectionScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    imageSourceId: Int,
    viewModel: TextSelectionViewModel = hiltViewModel()
) {
    val toastState = rememberToastState()
    val context = LocalContext.current
    val triggerCamera = remember { mutableStateOf(false) }
    val triggerGallery = remember { mutableStateOf(false) }
    val imageTexts = viewModel.imageTexts.collectAsState()
    val showError = viewModel.showError.collectAsState()
    val selectedTexts = remember { mutableStateOf(mutableSetOf<Int>()) }

    LaunchedEffect(Unit) {
        when (imageSourceId) {
            CAMERA_ID -> {
                triggerCamera.value = true
            }

            GALLERY_ID -> {
                triggerGallery.value = true
            }

            else -> {
                toastState.show(context.getString(R.string.something_went_wrong),
                    ToastType.ERROR,
                    onDismiss = {
                        navController.popBackStack()
                    })
            }
        }
    }

    if (showError.value) {
        toastState.show(context.getString(R.string.no_text_in_image),
            ToastType.ERROR,
            durationMillis = 1000,
            onDismiss = {
                viewModel.resetError()
                navController.popBackStack()
            })
    }

    if (triggerCamera.value) {
        CameraHandler(onImageCaptured = { bitmap ->
            triggerCamera.value = false
            if (bitmap == null) {
                toastState.show(context.getString(R.string.something_went_wrong),
                    ToastType.ERROR,
                    onDismiss = {
                        navController.popBackStack()
                    })
            } else {
                viewModel.fetchTextFromImage(bitmap)
                triggerCamera.value = false
            }
        }, onPermissionDenied = {
            triggerCamera.value = false
            toastState.show(context.getString(R.string.camera_permission_denied),
                ToastType.ERROR,
                onDismiss = {
                    navController.popBackStack()
                })
        })
    } else if (triggerGallery.value) {
        GalleryImagePicker(onImageSelected = { bitmap ->
            triggerGallery.value = false
            if (bitmap == null) {
                toastState.show(context.getString(R.string.something_went_wrong),
                    ToastType.ERROR,
                    onDismiss = {
                        navController.popBackStack()
                    })
            } else {
                viewModel.fetchTextFromImage(bitmap)
                triggerCamera.value = false
            }
        }, onPermissionDenied = {
            triggerCamera.value = false
            toastState.show(context.getString(R.string.gallery_permission_denied),
                ToastType.ERROR,
                onDismiss = {
                    navController.popBackStack()
                })
        })
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .border(
                width = 1.dp, color = Black, shape = RoundedCornerShape(8.dp)
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    text = context.getString(R.string.select_text),
                    textAlign = TextAlign.Center,
                    style = Typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            items(imageTexts.value.size) { index ->
                val text = imageTexts.value[index]
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .background(
                            color = if (selectedTexts.value.contains(index)) SelectedBlue else White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            val currentSet = selectedTexts.value.toMutableSet()
                            if (currentSet.contains(index)) {
                                currentSet.remove(index)
                            } else {
                                currentSet.add(index)
                            }
                            selectedTexts.value = currentSet
                        },
                    text = text,
                    textAlign = TextAlign.Center,
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = DividerGray,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .align(Alignment.Center)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(0.33f)
                .padding(12.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(24.dp)
                )
                .background(
                    color = White,
                    shape = RoundedCornerShape(24.dp)
                )
                .border(
                    width = 1.dp,
                    color = Black,
                    shape = RoundedCornerShape(24.dp)
                )
                .align(Alignment.BottomCenter)
                .clickable {
                    navController.navigate(
                        route = Screen.Translate.createRoute(
                            selectedText = selectedTexts.value.sorted()
                                .joinToString(separator = " ") { index ->
                                    imageTexts.value[index]
                                }
                        )
                    ) {
                        // Pop up to the current route (inclusive = true means including current screen)
                        popUpTo(
                            navController.currentBackStackEntry?.destination?.route
                                ?: return@navigate
                        ) {
                            inclusive = true
                        }
                    }
                }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = context.getString(R.string.confirm),
                textAlign = TextAlign.Center,
            )
        }
        ToastComponent(
            toastState = toastState
        )
    }
}