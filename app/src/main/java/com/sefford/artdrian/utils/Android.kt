package com.sefford.artdrian.utils

import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.sefford.artdrian.Artpplication
import com.sefford.artdrian.di.ApplicationComponent

val ComponentActivity.graph: ApplicationComponent
    get() = (this.application as Artpplication).graph

fun Color.toHex(): String = Integer.toHexString(this.toArgb())
