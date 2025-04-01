package com.numad.aitranslator.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.numad.aitranslator.R
import com.numad.aitranslator.ui.theme.Black
import com.numad.aitranslator.ui.theme.White

@Composable
fun SnackbarComponent(
    modifier: Modifier = Modifier,
    hostState: SnackbarHostState,
    buttons: List<SnackbarButton> = emptyList(),
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
        snackbar = {
            SnackbarView(
                buttons = buttons
            )
        }
    )
}

@Composable
private fun SnackbarView(
    modifier: Modifier = Modifier,
    buttons: List<SnackbarButton> = emptyList()
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(66.dp)
            .border(
                width = 1.dp,
                color = Black,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = White,
                shape = RoundedCornerShape(16.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly

    ) {
        buttons.forEachIndexed { index, button ->
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (index < buttons.size - 1) {
                            Modifier.border(
                                width = 0.dp,
                                color = Color.Transparent,
                                shape = RoundedCornerShape(0.dp)
                            )
                        } else {
                            Modifier
                        }
                    ),
                onClick = {
                    button.onClick(button.imageId)
                }
            ) {
                Image(
                    painter = painterResource(id = button.imageId),
                    contentDescription = button.description,
                    colorFilter = ColorFilter.tint(Black)
                )
            }

            if (index < buttons.size - 1) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(24.dp)
                        .background(Black)
                )
            }
        }
    }
}

data class SnackbarButton(
    val imageId: Int,
    val description: String,
    val onClick: (id: Int) -> Unit
)

@Preview(showBackground = true, showSystemUi = true, name = "Snackbar Component")
@Composable
private fun SnackbarViewPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        SnackbarView(
            buttons = listOf(
                SnackbarButton(
                    imageId = R.drawable.camera,
                    description = "camera",
                    onClick = {}
                ),
                SnackbarButton(
                    imageId = R.drawable.gallery,
                    description = "gallery",
                    onClick = {}
                )
            )
        )
    }
}