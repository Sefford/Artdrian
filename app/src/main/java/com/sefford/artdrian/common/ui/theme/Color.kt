package com.sefford.artdrian.common.ui.theme

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

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
