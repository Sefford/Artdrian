package com.sefford.utils

import java.nio.file.Paths

interface Files {


    fun <T> Class<T>.readResourceFromFile(fileName: String): String {
        return Paths.get(
            "src",
            *((listOf("test", "resources") + this.packageName.split(".") + fileName).toTypedArray())
        ).toFile().readText()
    }

    fun String.readResourceFromFile(): String {
        return Paths.get(
            "src",
            *((listOf("test", "resources") + this.split("/")).toTypedArray())
        ).toFile().readText()
    }

}
