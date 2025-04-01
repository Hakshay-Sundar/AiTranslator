package com.numad.aitranslator.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.numad.aitranslator.R
import com.numad.aitranslator.components.ToastComponent
import com.numad.aitranslator.components.ToastType
import com.numad.aitranslator.components.rememberToastState
import com.numad.aitranslator.utils.CAMERA_ID
import com.numad.aitranslator.utils.CameraHandler
import com.numad.aitranslator.utils.GALLERY_ID
import com.numad.aitranslator.viewmodels.TextSelectionViewModel

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
    val imageTexts = viewModel.imageTexts.collectAsState()

    LaunchedEffect(Unit) {
        when (imageSourceId) {
            CAMERA_ID -> {
                triggerCamera.value = true
            }

            GALLERY_ID -> {

            }

            else -> {
                toastState.show(
                    context.getString(R.string.something_went_wrong),
                    ToastType.ERROR,
                    onDismiss = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }

    if (triggerCamera.value) {
        CameraHandler(
            onImageCaptured = { bitmap ->
                viewModel.fetchTextFromImage(bitmap)
                triggerCamera.value = false
            },
            onPermissionDenied = {
                triggerCamera.value = false
                toastState.show(
                    context.getString(R.string.camera_permission_denied),
                    ToastType.ERROR,
                    onDismiss = {
                        navController.popBackStack()
                    }
                )
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column {
            imageTexts.value.forEach { text ->
                Text(text = text)
            }
        }
        ToastComponent(
            toastState = toastState
        )
    }
}