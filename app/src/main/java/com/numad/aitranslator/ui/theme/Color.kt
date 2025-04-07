package com.numad.aitranslator.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val DividerGray = Color(0xFFD9D9D9)
val SelectedBlue = Color(0x884052D6)

val PastelBlue = Color(0xFFB8D0E6)
val PastelPink = Color(0xFFF5D0E3)
val PastelGreen = Color(0xFFCCE6C4)
val PastelYellow = Color(0xFFF8EAC0)
val PastelPurple = Color(0xFFD9CAEE)
val PastelOrange = Color(0xFFFFD8C2)
val PastelTeal = Color(0xFFB8E6D9)
val PastelRed = Color(0xFFF5C6CB)

val pastelColors = listOf(
    PastelBlue,
    PastelPink,
    PastelGreen,
    PastelYellow,
    PastelPurple,
    PastelOrange,
    PastelTeal,
    PastelRed
)

fun getRandomPastelColor(): Color {
    return pastelColors.random()
}

fun Color.getVariationOfColor(): Color {
    val hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(
        this.toArgb(),
        hsv
    )

    // Slightly adjust hue (±5 degrees)
    hsv[0] = (hsv[0] + (Math.random() * 10 - 5).toFloat()) % 360

    // Slightly adjust saturation (±0.1) while keeping in valid range
    hsv[1] = (hsv[1] + (Math.random() * 0.2 - 0.1).toFloat()).coerceIn(0f, 1f)

    // Slightly adjust brightness (±0.1) while keeping in valid range
    hsv[2] = (hsv[2] + (Math.random() * 0.2 - 0.1).toFloat()).coerceIn(0f, 1f)

    return Color(android.graphics.Color.HSVToColor(hsv))
}

fun Color.getDarkerVariant(): Color {
    val hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(this.toArgb(), hsv)

    // Keep the same hue, increase saturation slightly, and decrease brightness to darken it
    hsv[1] = (hsv[1] * 1.2f).coerceAtMost(1f)
    hsv[2] = (hsv[2] * 0.7f)

    return Color(android.graphics.Color.HSVToColor(hsv))
}