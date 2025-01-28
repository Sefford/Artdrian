package com.sefford.artdrian.common.utils

import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager

fun WorkManager.Companion.initialize(context: Context, configuration: Configuration.Builder.() -> Unit) =
    initialize(context, Configuration.Builder().apply(configuration).build())
