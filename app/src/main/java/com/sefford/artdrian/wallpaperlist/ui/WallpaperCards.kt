package com.sefford.artdrian.wallpaperlist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sefford.artdrian.model.Wallpaper
import java.util.*

@Composable
fun WallpaperCard(wallpaper: Wallpaper) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.7f)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = wallpaper.desktop,
                contentDescription = wallpaper.metadata.slug,
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
                Text(text = wallpaper.metadata.views.toString())
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Rounded.Download, modifier = Modifier.size(20.dp), contentDescription = "")
                Text(text = wallpaper.metadata.downloads.toString())
            }
        }
    }
}

@Preview
@Composable
fun showPreviewCard() {
    WallpaperCard(
        Wallpaper(
            com.sefford.artdrian.model.Metadata("", "test", 123, 1000, Date(), Date()),
            "http://test",
            "http://test",
        )
    )
}
