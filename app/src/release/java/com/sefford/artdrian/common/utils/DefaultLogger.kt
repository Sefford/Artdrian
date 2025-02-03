package com.sefford.artdrian.common.utils

import coil3.util.Logger.Level
import javax.inject.Inject
import javax.inject.Singleton
import coil3.util.Logger as CoilLogger

@Singleton
class DefaultLogger private constructor(override var minLevel: Level = Level.Error) : Logger, CoilLogger {

    @Inject constructor(): this(Level.Error)

    override fun log(tag: String, message: String) {}

    override fun log(message: String) {}

    override fun log(tag: String, level: Level, message: String?, throwable: Throwable?) {}
}
