package com.numad.aitranslator.utils

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun GalleryImagePicker(
    onImageSelected: (Bitmap?) -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = getBitmapFromUri(context, uri)
            onImageSelected(bitmap)
        } ?: onImageSelected(null)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            galleryLauncher.launch("image/*")
        } else {
            onPermissionDenied()
        }
    }

    val openGallery = {
        when {
            // If the device is running Android 13 or above, we use the new permission
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
            // For older versions, also use READ_EXTERNAL_STORAGE
            else -> {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    // Trigger the gallery picker
    LaunchedEffect(Unit) {
        openGallery()
    }
}

// Helper function to get full-resolution bitmap from Uri
fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)

        val options = BitmapFactory.Options().apply {
            // Preserve image quality
            inJustDecodeBounds = false
            inSampleSize = 1
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }

        BitmapFactory.decodeStream(inputStream, null, options)?.also {
            inputStream?.close()
        }
    } catch (e: Exception) {
        Log.e("ImagePicker", "Error getting bitmap: ${e.message}")
        null
    }
}
