package com.sefford.artdrian.common.ui.theme

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat

@SuppressLint("NewApi")
@Composable
fun ArtdrianTheme(
    darkMode: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
    content: @Composable () -> Unit
) {
    // Determine the color scheme
    val context = LocalContext.current
    val colorScheme: ColorScheme = when {
        dynamicColor && darkMode -> dynamicDarkColorScheme(context)
        dynamicColor -> dynamicLightColorScheme(context)
        darkMode -> darkTheme
        else -> lightTheme
    }

    // Update the status bar color based on the theme
    val activity = LocalView.current.context as? Activity
    SideEffect {
        activity?.window?.apply {
            // Set Status Bar Color
            WindowCompat.getInsetsController(this, decorView).isAppearanceLightStatusBars = !darkMode

            // Set Navigation Bar Color
            WindowCompat.getInsetsController(this, decorView).isAppearanceLightNavigationBars = !darkMode
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
