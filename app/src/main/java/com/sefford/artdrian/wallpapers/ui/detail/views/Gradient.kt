package com.sefford.artdrian.wallpapers.ui.detail.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Gradient(modifier: Modifier = Modifier, mode: ContentMode, color: @Composable () -> Color) {
    val gradientOrigin: Float by animateFloatAsState(
        when (mode) {
            ContentMode.ACTIONS -> 0.8f
            ContentMode.INFO -> 0f
        }
    )
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                Brush.verticalGradient(
                    gradientOrigin to Color.Transparent,
                    1f to color()
                )
            )
            .padding(start = 16.dp)
            .clip(RoundedCornerShape(8.dp))
    ){}
}
