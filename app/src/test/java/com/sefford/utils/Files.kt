package com.sefford.utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.file.Path
import java.nio.file.Paths

interface Files {


    fun <T> Class<T>.readResourceFromFile(fileName: String): String {
        return Paths.get(
            "src",
            *((listOf("test", "resources") + this.packageName.split(".") + fileName).toTypedArray())
        ).toFile().readText()
    }

}
