package com.numad.aitranslator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.numad.aitranslator.R
import com.numad.aitranslator.ui.theme.Black
import com.numad.aitranslator.ui.theme.Pixelify
import com.numad.aitranslator.ui.theme.Typography
import com.numad.aitranslator.ui.theme.White

/**
 * A UI component that is used to showcase the language selected by the user or detected by the app.
 * */
@Composable
fun LanguageHolder(
    language: String? = null, onClick: () -> Unit, background: Color = White
) {
    Box(
        modifier = Modifier
            .size(height = 32.dp, width = 160.dp)
            .padding(vertical = 4.dp, horizontal = 12.dp)
            .border(width = 0.5.dp, color = Black, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(color = background)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = language ?: stringResource(R.string.select_language_description),
            style = Typography.bodySmall.copy(
                fontFamily = Pixelify
            )
        )
    }
}

@Preview(name = "Language Selected", showBackground = true)
@Composable
private fun LanguageSelectedPreview() {
    LanguageHolder(language = "English", onClick = {})
}

@Preview(name = "Language Not Selected", showBackground = true)
@Composable
private fun LanguageNotSelectedPreview() {
    LanguageHolder(language = null, onClick = {})
}