package com.sefford.artdrian.ui.navigation

import android.app.Activity
import android.content.Intent
import com.sefford.artdrian.wallpaperdetail.ui.WallpaperDetailActivity

fun Activity.goToDetail(wallpaperId: String, wallpaperName: String) {
    val intent = Intent(this, WallpaperDetailActivity::class.java)
    intent.putExtra(WallpaperDetailActivity.EXTRA_ID, wallpaperId)
    intent.putExtra(WallpaperDetailActivity.EXTRA_NAME, wallpaperName)
    startActivity(intent)
}
