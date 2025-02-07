package com.sefford.artdrian.wallpapers.ui.views

import androidx.compose.ui.graphics.Color

class WallpaperPalette(
    private val id: String,
    val dominant: Color,
    val onDominant: Color,
    val vibrant: Color,
    val onVibrant: Color,
    val muted: Color,
    val onMuted: Color,
    val light: Color,
    val onLight: Color,
    val dark: Color,
    val onDark: Color
) {
    companion object {
        private val CAUSTICS_001 = WallpaperPalette(
            id = "caustics_001",
            dominant = Color(0xFF181818), onDominant = Color.White,
            vibrant = Color(0xFFE05048), onVibrant = Color.Black,
            muted = Color(0xFFE05048), onMuted = Color.Black,
            light = Color(0xFFC8C0C8), onLight = Color.Black,
            dark = Color(0xFF000000), onDark = Color.White
        )

        private val GHOST_WAVES_001 = WallpaperPalette(
            id = "ghost_waves_001",
            dominant = Color(0xFFF0B0A8), onDominant = Color.Black,
            vibrant = Color(0xFFF08860), onVibrant = Color.Black,
            muted = Color(0xFFF08860), onMuted = Color.Black,
            light = Color(0xFF000000), onLight = Color.White,
            dark = Color(0xFF101010), onDark = Color.White
        )

        private val GHOST_WAVES_002 = WallpaperPalette(
            id = "ghost_waves_002",
            dominant = Color(0xFFD8E8F8), onDominant = Color.Black,
            vibrant = Color(0xFF6898F8), onVibrant = Color.White,
            muted = Color(0xFF6898F8), onMuted = Color.White,
            light = Color(0xFF000000), onLight = Color.White,
            dark = Color(0xFF000000), onDark = Color.White
        )

        private val GHOST_WAVES_003 = WallpaperPalette(
            id = "ghost_waves_003",
            dominant = Color(0xFFC0D0F0), onDominant = Color.Black,
            vibrant = Color(0xFF68A0E0), onVibrant = Color.White,
            muted = Color(0xFF68A0E0), onMuted = Color.White,
            light = Color(0xFF000000), onLight = Color.White,
            dark = Color(0xFF181818), onDark = Color.White
        )

        private val GHOST_WAVES_004 = WallpaperPalette(
            id = "ghost_waves_004",
            dominant = Color(0xFF101010), onDominant = Color.White,
            vibrant = Color(0xFF000000), onVibrant = Color.White,
            muted = Color(0xFF000000), onMuted = Color.White,
            light = Color(0xFFB0C0C0), onLight = Color.Black,
            dark = Color(0xFF000000), onDark = Color.White
        )

        private val DEFAULT_PALETTE = WallpaperPalette(
            id = "",
            dominant = Color(0xFF7C787C), onDominant = Color.White,
            vibrant = Color(0xFFB86068), onVibrant = Color.Black,
            muted = Color(0xFF6898F8), onMuted = Color.White,
            light = Color(0xFFC0C0C0), onLight = Color.Black,
            dark = Color(0xFF101010), onDark = Color.White
        )

        private val PALETTES = listOf(CAUSTICS_001, GHOST_WAVES_001, GHOST_WAVES_002, GHOST_WAVES_003, GHOST_WAVES_004)
            .associateBy { it.id }
            .withDefault { DEFAULT_PALETTE }


        operator fun get(slug: String): WallpaperPalette = PALETTES.getValue(slug)

        operator fun get(index: Int): WallpaperPalette = PALETTES.values.toList()[index]
    }
}
