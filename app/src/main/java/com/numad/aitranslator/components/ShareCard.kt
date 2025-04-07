package com.numad.aitranslator.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.numad.aitranslator.R
import com.numad.aitranslator.room.entities.TranslationEntity
import com.numad.aitranslator.ui.theme.DividerGray
import com.numad.aitranslator.ui.theme.Pixelify
import com.numad.aitranslator.ui.theme.Typography
import com.numad.aitranslator.ui.theme.White
import com.numad.aitranslator.ui.theme.getDarkerVariant
import com.numad.aitranslator.ui.theme.getRandomPastelColor
import com.numad.aitranslator.ui.theme.getVariationOfColor
import kotlinx.coroutines.launch

@Composable
fun ShareCard(
    modifier: Modifier = Modifier,
    translation: TranslationEntity?,
    pastelColor: Color = getRandomPastelColor(),
    onShareClick: (Bitmap) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .background(
                color = DividerGray,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = modifier
                .drawWithContent {
                    graphicsLayer.record {
                        this@drawWithContent.drawContent()
                    }
                    drawLayer(graphicsLayer)
                }
                .background(
                    color = White,
                    shape = RoundedCornerShape(24.dp)
                )
                .border(
                    width = 1.dp,
                    color = pastelColor.getDarkerVariant(),
                    shape = RoundedCornerShape(24.dp)
                )
                .clip(RoundedCornerShape(24.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = White)
                    .padding(horizontal = 4.dp, vertical = 8.dp),
            ) {
                Image(
                    modifier = Modifier.size(50.dp),
                    painter = painterResource(R.drawable.appicon),
                    contentDescription = stringResource(R.string.app_name),
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically),
                    text = stringResource(R.string.app_name),
                    textAlign = TextAlign.Center,
                    style = Typography.titleSmall
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.Absolute.SpaceEvenly
            ) {
                ShareLanguageHolder(
                    language = translation?.languageFrom.orEmpty(),
                    color = pastelColor
                )
                ArrowComponent(
                    modifier = Modifier
                        .size(width = 36.dp, height = 24.dp)
                        .align(Alignment.CenterVertically),
                    color = pastelColor.getVariationOfColor()
                )
                ShareLanguageHolder(
                    language = translation?.languageTo.orEmpty(),
                    color = pastelColor
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = context.getString(R.string.input),
                    style = Typography.titleLarge.copy(
                        fontSize = 14.sp
                    )
                )
                Text(
                    text = translation?.text.orEmpty(),
                    style = Typography.bodyMedium.copy(
                        fontSize = 10.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = pastelColor.getDarkerVariant()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = context.getString(R.string.translation),
                    style = Typography.titleLarge.copy(
                        fontSize = 14.sp
                    )
                )
                Text(
                    text = translation?.translatedText.orEmpty(),
                    style = Typography.bodyMedium.copy(
                        fontSize = 10.sp
                    )
                )
            }
        }
        Row {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                onClick = {
                    coroutineScope.launch {
                        val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()
                        onShareClick(bitmap)
                    }
                }
            ) {
                Text(text = "Share")
            }
        }
    }

}

@Composable
private fun ShareLanguageHolder(modifier: Modifier = Modifier, color: Color, language: String) {
    Box(
        modifier = modifier
            .height(32.dp)
            .defaultMinSize(minWidth = 80.dp)
            .padding(vertical = 4.dp, horizontal = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = color),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = language.ifEmpty { "" },
            style = Typography.bodySmall.copy(
                fontFamily = Pixelify
            ),
            color = color.getDarkerVariant()
        )
    }
}

@Preview()
@Composable
private fun ShareCardPreview(modifier: Modifier = Modifier) {
    ShareCard(
        modifier = modifier,
        translation = TranslationEntity(
            id = 1,
            text = "Hello",
            translatedText = "Hola",
            languageFrom = "English",
            languageTo = "Spanish",
            timestampMillis = System.currentTimeMillis()
        ),
        onShareClick = { _ -> }
    )
}