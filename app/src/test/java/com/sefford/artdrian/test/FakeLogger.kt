package com.sefford.artdrian.test

import coil3.util.Logger.Level
import com.sefford.artdrian.common.utils.Logger

class FakeLogger(
    private val _messages: MutableList<Pair<String, String>> = mutableListOf(),
    override var minLevel: Level = Level.Verbose
) : Logger {

    val messages: List<Pair<String, String>>
        get() = _messages.toList()

    override fun log(tag: String, message: String) {
        println("$tag: $message")
        _messages.add(tag to message)
    }

    override fun log(message: String) {
        println(message)
        _messages.add("" to message)
    }

    override fun log(tag: String, level: Level, message: String?, throwable: Throwable?) {
        println("$tag ${message.orEmpty()}")
        _messages.add(tag to message.orEmpty())
    }
}
