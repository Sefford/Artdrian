package com.sefford.artdrian.wallpapers.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

@Composable
fun WallpaperImage(
    modifier: Modifier,
    image: ImageRequest
) {
    AsyncImage(
        modifier = modifier,
        model = image.url,
        placeholder = ColorPainter(image.placeholder),
        contentDescription = image.description,
        contentScale = image.scale,
    )
}

class ImageRequest(
    val url: String,
    val description: String,
    val placeholder: Color,
    val scale: ContentScale
)
