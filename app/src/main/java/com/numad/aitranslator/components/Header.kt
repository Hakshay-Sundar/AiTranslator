package com.numad.aitranslator.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.numad.aitranslator.R
import com.numad.aitranslator.ui.theme.Typography
import com.numad.aitranslator.ui.theme.White

@Composable
fun Header(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = White)
            .padding(horizontal = 24.dp, vertical = 12.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.appicon),
            contentDescription = stringResource(R.string.app_name),
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically),
            text = stringResource(R.string.app_name),
            textAlign = TextAlign.Center,
            style = Typography.titleLarge
        )
    }
}

@Composable
@Preview(name = "Header Preview", showBackground = true)
private fun HeaderPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = androidx.compose.ui.graphics.Color.Blue),
    ) {
        Header()
    }
}