package com.sefford.artdrian.common.utils

import io.ktor.client.plugins.logging.Logger

interface Logger: Logger {
    fun debug(tag: String, message: String)
}

