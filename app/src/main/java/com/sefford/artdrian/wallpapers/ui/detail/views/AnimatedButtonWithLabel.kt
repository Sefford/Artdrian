package com.sefford.artdrian.wallpapers.ui.detail.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedButtonWithLabel(
    mode: ContentMode,
    content: @Composable () -> Unit,
) {
    AnimatedContent(
        targetState = mode,
        transitionSpec = { scaleIn() togetherWith scaleOut() }
    ) { animatedMode ->
        when (animatedMode) {
            ContentMode.ACTIONS -> content()
            ContentMode.INFO -> Spacer(modifier = Modifier.width(64.dp))
        }
    }
}
