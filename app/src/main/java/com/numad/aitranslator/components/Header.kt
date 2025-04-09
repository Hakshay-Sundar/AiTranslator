package com.numad.aitranslator.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.numad.aitranslator.R
import com.numad.aitranslator.ui.theme.Black
import com.numad.aitranslator.ui.theme.Typography
import com.numad.aitranslator.ui.theme.White

/**
 * A UI component that renders a custom toolbar with no tools.
 * It is used as the top bar of the application.
 * */
@Composable
fun Header() {
    Surface(
        modifier = Modifier,
        shape = RoundedCornerShape(
            bottomStart = 9.dp, bottomEnd = 9.dp
        ),
        shadowElevation = 1.dp,
        color = Color.Black.copy(alpha = 0.2f)
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                    .background(color = Black)
                    .padding(horizontal = 24.dp, vertical = 12.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.appicon),
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier
                        .size(120.dp)
                        .background(
                            color = White,
                            shape = CircleShape
                        )
                        .padding(24.dp)

                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically),
                    text = stringResource(R.string.app_name),
                    textAlign = TextAlign.Center,
                    style = Typography.titleLarge.copy(
                        fontSize = 28.sp
                    ),
                    color = White
                )
            }
        }
    }
}

@Composable
@Preview(name = "Header Preview", showBackground = true)
private fun HeaderPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White),
    ) {
        Header()
    }
}