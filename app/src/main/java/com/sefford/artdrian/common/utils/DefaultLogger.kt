package com.sefford.artdrian.common.utils

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultLogger @Inject constructor() : Logger {

    override fun debug(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun log(message: String) {
        Log.d("Logger Ktor =>", message)
    }
}
