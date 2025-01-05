package com.sefford.artdrian.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        contentDescription = image.description,
        contentScale = image.scale,
    )
}

class ImageRequest(
    val url: String,
    val description: String,
    val scale: ContentScale
)
