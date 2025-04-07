package com.numad.aitranslator.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

/**
 * This is an extension function for the Context class.
 * It takes a bitmap as input, saves it to the cache directory, and shares it using an implicit intent.
 * @param bitmap The bitmap to be shared.
 * */
fun Context.shareBitmap(bitmap: Bitmap) {
    val imagesFolder = File(cacheDir, "images")
    imagesFolder.mkdirs()

    val file = File(imagesFolder, "shared_translation_${System.currentTimeMillis()}.png")
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        out.flush()
    }

    val uri = FileProvider.getUriForFile(
        this,
        "$packageName.fileprovider",
        file
    )

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_TEXT, "Shared from AiTranslator")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    startActivity(Intent.createChooser(shareIntent, "Share Translation"))
}
