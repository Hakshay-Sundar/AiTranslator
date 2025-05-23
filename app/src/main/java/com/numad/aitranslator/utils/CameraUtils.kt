package com.numad.aitranslator.utils

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

const val CAMERA_ID = 101
const val GALLERY_ID = 102

/**
 * This is a utility function that is used to invoke the camera.
 * It checks if the application has the permission to access the camera.
 * In case, the application does not, it requests the permission.
 * If the permission exists, it fetches the image from the camera.
 * @param onImageCaptured This is a callback that is invoked when the image is captured.
 * @param onPermissionDenied This is a callback that is invoked when the permission is denied.
 * @return a composable function that handles access to the camera and invokes a callback based
 * on the state of the application.
 * */
@Composable
fun CameraHandler(
    onImageCaptured: (Bitmap?) -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current
    val hasPermission = remember {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Create a temporary file to store the captured image.
    val tempImageFile = remember {
        File.createTempFile(
            "camera_photo_",
            ".jpg",
            context.cacheDir
        ).apply {
            deleteOnExit()
        }
    }

    // Fetch the URI of the temporary file.
    val imageUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            tempImageFile
        )
    }

    // TakePicture returns a path using which we can get a full resolution image.
    // We can pass the bitmap to the model to extract text.
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // Convert the saved image to bitmap
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            // Pass the full-resolution bitmap to the callback
            bitmap?.let {
                onImageCaptured(it)

                // Delete the temp file
                tempImageFile.delete()
            }
        } else {
            onImageCaptured(null)
        }
    }

    fun launchCamera() {
        cameraLauncher.launch(imageUri)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCamera()
        } else {
            // Permission denied. Need to handle gracefully.
            onPermissionDenied()
        }
    }

    // Launch the camera when the permission is granted.
    LaunchedEffect(Unit) {
        if (hasPermission) {
            launchCamera()
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}
