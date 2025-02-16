package com.numad.aitranslator.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ArrowComponent(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    strokeWidth: Float = 5f,
    angle: Double = Math.toRadians(45.0)
) {
    Box(
        modifier = modifier
            .drawBehind {
                val start = Offset(0f, size.height / 2)
                val end = Offset(size.width, size.height / 2)
                val path = Path().apply {
                    moveTo(start.x, start.y)
                    lineTo(end.x, end.y)

                    // Draw the arrowhead
                    val arrowSize = size.height / 4
                    val dx = end.x - start.x
                    val dy = end.y - start.y
                    val lineLength = kotlin.math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
                    val unitDx = dx / lineLength
                    val unitDy = dy / lineLength

                    val arrowX1 =
                        end.x - arrowSize * (unitDx * cos(angle) - unitDy * sin(angle)).toFloat()
                    val arrowY1 =
                        end.y - arrowSize * (unitDx * sin(angle) + unitDy * cos(angle)).toFloat()
                    val arrowX2 =
                        end.x - arrowSize * (unitDx * cos(-angle) - unitDy * sin(-angle)).toFloat()
                    val arrowY2 =
                        end.y - arrowSize * (unitDx * sin(-angle) + unitDy * cos(-angle)).toFloat()

                    lineTo(arrowX1, arrowY1)
                    moveTo(end.x, end.y)
                    lineTo(arrowX2, arrowY2)
                }
                drawPath(path, color, style = Stroke(strokeWidth))
            }
    )
}

@Preview(name = "Right Arrow Preview", showBackground = true)
@Composable
private fun ArrowComponentRightPreview() {
    ArrowComponent(
        modifier = Modifier.size(100.dp),
        strokeWidth = 10f,
        angle = Math.toRadians(30.0)
    )
}