package com.sefford.artdrian.common


class FakeFileManager(private val response: () -> String): FileManager {

    override suspend fun saveFileIntoDirectory(source: String, target: String): String {
        return response()
    }

}
