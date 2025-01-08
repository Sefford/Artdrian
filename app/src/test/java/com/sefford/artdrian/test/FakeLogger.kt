package com.sefford.artdrian.test

import com.sefford.artdrian.common.utils.Logger

class FakeLogger: Logger {
    override fun debug(tag: String, message: String) {
        println("$tag: $message")
    }

    override fun log(message: String) {
        println(message)
    }
}
