package com.sefford.artdrian.test

import com.sefford.artdrian.common.utils.Logger

class FakeLogger(private val _messages: MutableList<Pair<String, String>> = mutableListOf()): Logger {

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
}
