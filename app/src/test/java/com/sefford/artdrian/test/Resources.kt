package com.sefford.artdrian.test

interface Resources {

    fun String.asResource(): String = object {}.javaClass.classLoader!!.getResource(this).readText()

    fun String.asResponse() = "responses/$this".asResource()

    fun String.asJson() = "json/$this".asResource()

}
