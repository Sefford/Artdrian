package com.sefford.artdrian.common

interface FileManager {

    suspend fun saveFileIntoDirectory(source: String, target: String): String

}
