package com.sefford.artdrian.utils

import io.ktor.client.plugins.logging.Logger

interface Logger: Logger {
    fun debug(tag: String, message: String)
}

