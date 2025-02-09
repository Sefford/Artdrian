package com.sefford.artdrian.wallpapers.ui.detail.views

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

@Composable
fun Gradient(modifier: Modifier = Modifier, mode: ContentMode, blur: HazeState, color: @Composable () -> Color) {
    val origin: Float by animateFloatAsState(
        when (mode) {
            ContentMode.ACTIONS -> .8f
            ContentMode.INFO -> 0f
        }
    )
    val intensity: Float by animateFloatAsState(
        when (mode) {
            ContentMode.ACTIONS -> .1f
            ContentMode.INFO -> 0f
        }
    )
    var height by remember { mutableIntStateOf(0) }
    val style = HazeStyle(
        backgroundColor = Color.Transparent,
        tints = listOf(HazeTint(color().copy(alpha = .4f))),
        blurRadius = HazeDefaults.blurRadius,
        noiseFactor = HazeDefaults.noiseFactor,
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .onSizeChanged { size ->
                height = size.height
            }
            .hazeEffect(state = blur, style = style) {
                progressive = HazeProgressive.verticalGradient(
                    startIntensity = intensity,
                    startY = height * origin,
                    endIntensity = 1f,
                )
            }
            .padding(start = 16.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {}
}
