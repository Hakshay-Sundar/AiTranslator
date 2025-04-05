package com.numad.aitranslator.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.numad.aitranslator.R

@Composable
fun ClickableImage(
    modifier: Modifier = Modifier,
    imageId: Int,
    descriptionId: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = stringResource(id = descriptionId),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(name = "Example Image", showBackground = true)
@Composable
private fun ClickableImagePreview() {
    ClickableImage(
        imageId = R.drawable.mic,
        descriptionId = R.string.mic_description,
        onClick = {
            // Handle click event
            // You can add your logic here
        }
    )
}