package com.numad.aitranslator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.numad.aitranslator.R
import com.numad.aitranslator.navigation.TranslateScreenParams
import com.numad.aitranslator.ui.theme.Black
import com.numad.aitranslator.ui.theme.White

/**
 * UI Component that renders the bottom navigation bar of the application.
 * In the context of this application, it gives the user access to 2 modes of translation:
 * 1. Text to Translation
 * 2. Image to Text to Translation
 * */
@Composable
fun Footer(
    modifier: Modifier,
    onTabClick: (String) -> Unit = {}
) {
    Surface(
        modifier = Modifier,
        shape = RoundedCornerShape(
            topStart = 9.dp, topEnd = 9.dp
        ),
        shadowElevation = 3.dp,
        color = Color.Black.copy(alpha = 0.2f)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(color = White)
                    .border(
                        width = 1.dp,
                        color = Black,
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                    )
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ClickableImage(
                    imageId = R.drawable.text_edit,
                    descriptionId = R.string.text_edit_description
                ) {
                    onTabClick(TranslateScreenParams.TEXT_TO_TRANSLATION)
                }
                ClickableImage(
                    imageId = R.drawable.photo,
                    descriptionId = R.string.image_edit_description
                ) {
                    onTabClick(TranslateScreenParams.IMAGE_TO_TRANSLATION)
                }
            }
        }
    }
}

@Preview(name = "FooterView", showBackground = true, showSystemUi = true)
@Composable
private fun FooterView() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Footer(
            modifier = Modifier
        )
    }
}