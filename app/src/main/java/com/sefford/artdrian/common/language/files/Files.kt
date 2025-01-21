package com.sefford.artdrian.common.language.files

import okio.FileSystem
import okio.Path.Companion.toOkioPath
import okio.buffer
import okio.sink
import java.io.File

fun File.buffer() = FileSystem.SYSTEM.source(toOkioPath()).buffer()

fun File.clear() = FileSystem.SYSTEM.write(file = toOkioPath()) {}

fun File.contentToString() = buffer().use { stream -> stream.readUtf8() }

fun File.writeString(content: String) = sink().buffer().use { stream -> stream.writeUtf8(content)}

