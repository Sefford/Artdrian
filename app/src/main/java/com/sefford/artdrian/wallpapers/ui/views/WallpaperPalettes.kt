package com.sefford.artdrian.wallpapers.ui.views

import androidx.compose.ui.graphics.Color

class WallpaperPalette(
    private val id: String,
    val dominant: Color,
    val vibrant: Color,
    val muted: Color,
    val light: Color,
    val dark: Color
) {
    companion object {
        private val CAUSTICS_001 = WallpaperPalette(
            id = "caustics_001",
            dominant = Color(0xFF181818),
            vibrant = Color(0xFFE05048),
            muted = Color(0xFFE05048),
            light = Color(0xFFC8C0C8),
            dark = Color(0xFF000000)
        )

        private val GHOST_WAVES_001 = WallpaperPalette(
            id = "ghost_waves_001",
            dominant = Color(0xFFF0B0A8),
            vibrant = Color(0xFFF08860),
            muted = Color(0xFFF08860),
            light = Color(0xFF000000),
            dark = Color(0xFF101010)
        )

        private val GHOST_WAVES_002 = WallpaperPalette(
            id = "ghost_waves_002",
            dominant = Color(0xFFD8E8F8),
            vibrant = Color(0xFF6898F8),
            muted = Color(0xFF6898F8),
            light = Color(0xFF000000),
            dark = Color(0xFF000000)
        )

        private val GHOST_WAVES_003 = WallpaperPalette(
            id = "ghost_waves_003",
            dominant = Color(0xFFC0D0F0),
            vibrant = Color(0xFF68A0E0),
            muted = Color(0xFF68A0E0),
            light = Color(0xFF000000),
            dark = Color(0xFF181818)
        )

        private val GHOST_WAVES_004 = WallpaperPalette(
            id = "ghost_waves_004",
            dominant = Color(0xFF101010),
            vibrant = Color(0xFF000000),
            muted = Color(0xFF000000),
            light = Color(0xFFB0C0C0),
            dark = Color(0xFF000000)
        )

        private val DEFAULT_PALETTE = WallpaperPalette(
            id = "",
            dominant = Color(0xFF7C787C), // Average of dominant colors
            vibrant = Color(0xFFB86068),  // Average of vibrant colors
            muted = Color(0xFF6898F8),    // Kept since 3/5 had similar values
            light = Color(0xFFC0C0C0),    // Average of light muted colors
            dark = Color(0xFF101010)      // Common dark muted color
        )


        private val PALETTES = listOf(CAUSTICS_001, GHOST_WAVES_001, GHOST_WAVES_002, GHOST_WAVES_003, GHOST_WAVES_004)
            .associateBy { it.id }
            .withDefault { DEFAULT_PALETTE }


        operator fun get(slug: String): WallpaperPalette = PALETTES.getValue(slug)
    }
}
