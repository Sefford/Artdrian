package com.sefford.artdrian.common.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun ThemePreview() {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Theme Preview", color = colors.onBackground, style = MaterialTheme.typography.headlineMedium)

        ColorBlock("Primary", colors.primary, colors.onPrimary)
        ColorBlock("Primary Container", colors.primaryContainer, colors.onPrimaryContainer)
        ColorBlock("Inverse Primary", colors.inversePrimary, colors.onPrimary)

        ColorBlock("Secondary", colors.secondary, colors.onSecondary)
        ColorBlock("Secondary Container", colors.secondaryContainer, colors.onSecondaryContainer)

        ColorBlock("Tertiary", colors.tertiary, colors.onTertiary)
        ColorBlock("Tertiary Container", colors.tertiaryContainer, colors.onTertiaryContainer)

        ColorBlock("Background", colors.background, colors.onBackground)
        ColorBlock("Surface", colors.surface, colors.onSurface)
        ColorBlock("Surface Variant", colors.surfaceVariant, colors.onSurfaceVariant)

        ColorBlock("Inverse Surface", colors.inverseSurface, colors.inverseOnSurface)

        ColorBlock("Error", colors.error, colors.onError)
        ColorBlock("Error Container", colors.errorContainer, colors.onErrorContainer)

        ColorBlock("Outline", colors.outline, colors.onBackground)
        ColorBlock("Outline Variant", colors.outlineVariant, colors.onBackground)

        ColorBlock("Scrim", colors.scrim, Color.White)
    }
}

@Composable
private fun ColorBlock(label: String, backgroundColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
