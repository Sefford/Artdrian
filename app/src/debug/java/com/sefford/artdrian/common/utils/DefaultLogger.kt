package com.sefford.artdrian.common.utils

import android.util.Log
import coil3.util.DebugLogger
import coil3.util.Logger.Level
import javax.inject.Inject
import javax.inject.Singleton
import coil3.util.Logger as CoilLogger

@Singleton
class DefaultLogger @Inject constructor() : Logger, CoilLogger by DebugLogger() {

    override fun log(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun log(message: String) {
        Log.d("Logger Ktor =>", message)
    }
}
