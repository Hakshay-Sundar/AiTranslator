package com.numad.aitranslator.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.numad.aitranslator.ui.theme.White
import kotlinx.coroutines.delay

@Composable
fun ToastComponent(
    toastState: ToastState,
    modifier: Modifier = Modifier,
    duration: Long = 3000
) {
    // Auto-dismiss after specified duration when visible
    LaunchedEffect(key1 = toastState.isVisible) {
        if (toastState.isVisible) {
            delay(duration)
            toastState.hide()
            toastState.onDismiss()
        }
    }

    // Get color, icon and content description based on toast type
    val (backgroundColor, icon, contentDescription) = when (toastState.type) {
        ToastType.SUCCESS -> Triple(
            Color(0xFF4CAF50),
            Icons.Default.Check,
            "Success"
        )

        ToastType.WARNING -> Triple(
            Color(0xFFFFA000),
            Icons.Default.Warning,
            "Warning"
        )

        ToastType.ERROR -> Triple(
            Color(0xFFF44336),
            Icons.Default.Clear,
            "Error"
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = toastState.isVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ToastContent(
                message = toastState.message,
                backgroundColor = backgroundColor,
                contentColor = White,
                icon = icon,
                contentDescription = contentDescription
            )
        }
    }
}

@Composable
private fun ToastContent(
    message: String,
    backgroundColor: Color,
    contentColor: Color,
    icon: ImageVector,
    contentDescription: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = contentColor
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = message,
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f)
        )
    }
}

// Helper function to remember toast state
@Composable
fun rememberToastState(): ToastState {
    return remember { ToastState() }
}

class ToastState {
    var message by mutableStateOf("")
    var type by mutableStateOf(ToastType.SUCCESS)
    var isVisible by mutableStateOf(false)
    var onDismiss: () -> Unit = { isVisible = false }

    fun show(message: String, type: ToastType, durationMillis: Long = 3000, onDismiss: () -> Unit = {}) {
        this.message = message
        this.type = type
        this.isVisible = true
        this.onDismiss = onDismiss
    }

    fun hide() {
        isVisible = false
    }
}

enum class ToastType {
    SUCCESS,
    WARNING,
    ERROR
}

@Preview(showBackground = true)
@Composable
fun ToastComponentPreview() {
    val toastState = rememberToastState()

    LaunchedEffect(Unit) {
        toastState.show("This is a success toast", ToastType.SUCCESS)
        delay(3000)
        toastState.show("This is a warning toast", ToastType.WARNING)
        delay(3000)
        toastState.show("This is an error toast", ToastType.ERROR)
    }

    ToastComponent(toastState)
}