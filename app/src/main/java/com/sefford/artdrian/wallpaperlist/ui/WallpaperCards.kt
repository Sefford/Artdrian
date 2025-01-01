package com.sefford.artdrian.wallpaperlist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sefford.artdrian.model.Wallpaper
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun WallpaperCard(
    wallpaper: Wallpaper,
    onItemClicked: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.7f)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .testTag(wallpaper.id)
            .clickable(onClick = onItemClicked)) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = wallpaper.desktop,
                contentDescription = wallpaper.slug,
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(8.dp, 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Icon(Icons.Rounded.Visibility, modifier = Modifier.size(20.dp), contentDescription = "")
                Text(text = wallpaper.views.toString())
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Rounded.Download, modifier = Modifier.size(20.dp), contentDescription = "")
                Text(text = wallpaper.downloads.toString())
            }
        }
    }
}

@Preview
@Composable
fun showPreviewCard() {
    WallpaperCard(
        Wallpaper.FromLocal("", "title", 98, 123, "test", Clock.System.now().toLocalDateTime(TimeZone.UTC))
    )
}
