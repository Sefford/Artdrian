package com.sefford.artdrian.common.utils

import io.ktor.client.plugins.logging.Logger

interface Logger: Logger, coil3.util.Logger {
    fun log(tag: String, message: String)
}

