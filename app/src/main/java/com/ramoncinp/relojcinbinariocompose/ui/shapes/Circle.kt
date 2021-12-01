package com.ramoncinp.relojcinbinariocompose.ui.shapes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp

private const val STROKE_WIDTH = 2f

@Composable
fun Circle(isActive: Boolean = true) {
    val color = MaterialTheme.colors.primary
    val drawStyle = if (isActive) Fill else Stroke(width = STROKE_WIDTH)

    Canvas(modifier = Modifier.size(40.dp).padding(8.dp)) {
        drawCircle(color = color, style = drawStyle)
    }
}
