package com.sefford.artdrian.common.ui.theme

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat

val darkTheme: ColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF3700B3),
    onPrimaryContainer = Color.White,
    inversePrimary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC6),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF018786),
    onSecondaryContainer = Color.White,
    tertiary = Color(0xFF03DAC6),
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF018786),
    onTertiaryContainer = Color.White,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF121212),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF333333),
    onSurfaceVariant = Color.White,
    surfaceTint = Color(0xFFBB86FC),
    inverseSurface = Color(0xFFF5F5F5),
    inverseOnSurface = Color.Black,
    error = Color(0xFFCF6679),
    onError = Color.Black,
    errorContainer = Color(0xFFB00020),
    onErrorContainer = Color(0xFFD7A9A1),
    outline = Color(0xFF9E9E9E),
    outlineVariant = Color(0xFF616161),
    scrim = Color.Black,
    surfaceBright = Color(0xFF424242),
    surfaceContainer = Color(0xFF333333),
    surfaceContainerHigh = Color(0xFF424242),
    surfaceContainerHighest = Color(0xFF424242),
    surfaceContainerLow = Color(0xFF616161),
    surfaceContainerLowest = Color(0xFF757575),
    surfaceDim = Color(0xFF9E9E9E)
)

val lightTheme: ColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBB86FC),
    onPrimaryContainer = Color.Black,
    inversePrimary = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC6),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF018786),
    onSecondaryContainer = Color.White,
    tertiary = Color(0xFF03DAC6),
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF018786),
    onTertiaryContainer = Color.White,
    background = Color(0xFFF5F5F5),
    onBackground = Color.Black,
    surface = Color(0xFFFFFFFF),
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFEEEEEE),
    onSurfaceVariant = Color.Black,
    surfaceTint = Color(0xFF6200EE),
    inverseSurface = Color(0xFF121212),
    inverseOnSurface = Color.White,
    error = Color(0xFFB00020),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD4),
    onErrorContainer = Color(0xFF410E0B),
    outline = Color(0xFFBDBDBD),
    outlineVariant = Color(0xFF616161),
    scrim = Color.Black,
    surfaceBright = Color(0xFFFFFFFF),
    surfaceContainer = Color(0xFFFFFFFF),
    surfaceContainerHigh = Color(0xFFF5F5F5),
    surfaceContainerHighest = Color(0xFFF5F5F5),
    surfaceContainerLow = Color(0xFFE0E0E0),
    surfaceContainerLowest = Color(0xFFBDBDBD),
    surfaceDim = Color(0xFF9E9E9E)
)


@Composable
fun ArtdrianTheme(
    darkMode: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // Determine the color scheme
    val context = LocalContext.current
    val androidS = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colorScheme: ColorScheme = when {
        dynamicColor && androidS && darkMode -> dynamicDarkColorScheme(context)
        dynamicColor && androidS -> dynamicLightColorScheme(context)
        darkMode -> darkTheme
        else -> lightTheme
    }

    // Update the status bar color based on the theme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkMode
        }
    }

    // Apply MaterialTheme with the determined colorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = artdrianTypography,
        content = content
    )
}

object ArtdrianTheme {
    val typography: Typography
        @Composable
        get() = artdrianTypography

    val colors: ColorScheme
        @Composable
        get() = MaterialTheme.colorScheme
}

@Composable
private fun ThemePreview() {
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

@Composable
@Preview(name = "Light Mode", heightDp = 1000)
@Preview(
    name = "Dark Mode",
    heightDp = 1000,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor =
    0xFF000000
)
fun PreviewThemePreview() {
    ArtdrianTheme(darkMode = isSystemInDarkTheme()) {
        ThemePreview()
    }
}
