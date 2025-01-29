package com.sefford.artdrian.common.utils

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultLogger @Inject constructor() : Logger {

    override fun debug(tag: String, message: String) {}

    override fun log(message: String) {}
}
