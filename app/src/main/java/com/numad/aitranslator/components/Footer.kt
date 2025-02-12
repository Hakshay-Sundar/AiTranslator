package com.numad.aitranslator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.numad.aitranslator.Navigation.ScreenInitParams
import com.numad.aitranslator.R
import com.numad.aitranslator.ui.theme.Black
import com.numad.aitranslator.ui.theme.Pink40
import com.numad.aitranslator.ui.theme.Pink80
import com.numad.aitranslator.ui.theme.White

@Composable
fun Footer(
    modifier: Modifier,
    onTabClick: (String) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 64.dp,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                ambientColor = Pink40,
                spotColor = Pink80
            )
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
            imageId = R.drawable.mic,
            descriptionId = R.string.mic_description
        ) {
            onTabClick(ScreenInitParams.AudioToTranslation.type)
        }
        ClickableImage(
            imageId = R.drawable.text_edit,
            descriptionId = R.string.text_edit_description
        ) {
            onTabClick(ScreenInitParams.TextToTranslation.type)
        }
        ClickableImage(
            imageId = R.drawable.photo,
            descriptionId = R.string.image_edit_description
        ) {
            onTabClick(ScreenInitParams.ImageToTranslation.type)
        }
    }
}

@Preview(name = "FooterView", showBackground = true, showSystemUi = true)
@Composable
fun FooterView() {
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