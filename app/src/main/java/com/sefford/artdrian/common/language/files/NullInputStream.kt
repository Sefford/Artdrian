package com.sefford.artdrian.common.language.files

import java.io.EOFException
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.Objects
import kotlin.concurrent.Volatile

/** Port from Java 11 */
class NullInputStream : InputStream() {
    @Volatile
    private var closed = false

    @Throws(IOException::class)
    private fun ensureOpen() {
        if (closed) {
            throw IOException("Stream closed")
        }
    }

    @Throws(IOException::class)
    override fun available(): Int {
        ensureOpen()
        return 0
    }

    @Throws(IOException::class)
    override fun read(): Int {
        ensureOpen()
        return -1
    }

    @Throws(IOException::class)
    override fun read(b: ByteArray, off: Int, len: Int): Int {
        Objects.checkFromIndexSize(off, len, b.size)
        if (len == 0) {
            return 0
        }
        ensureOpen()
        return -1
    }

    @Throws(IOException::class)
    override fun readAllBytes(): ByteArray {
        ensureOpen()
        return ByteArray(0)
    }

    @Throws(IOException::class)
    override fun readNBytes(b: ByteArray, off: Int, len: Int): Int {
        Objects.checkFromIndexSize(off, len, b.size)
        ensureOpen()
        return 0
    }

    @Throws(IOException::class)
    override fun readNBytes(len: Int): ByteArray {
        require(len >= 0) { "len < 0" }
        ensureOpen()
        return ByteArray(0)
    }

    @Throws(IOException::class)
    override fun skip(n: Long): Long {
        ensureOpen()
        return 0L
    }

    @Throws(IOException::class)
    override fun skipNBytes(n: Long) {
        ensureOpen()
        if (n > 0) {
            throw EOFException()
        }
    }

    @Throws(IOException::class)
    override fun transferTo(out: OutputStream): Long {
        Objects.requireNonNull(out)
        ensureOpen()
        return 0L
    }

    @Throws(IOException::class)
    override fun close() {
        closed = true
    }
}

val OutputStream.isNull: Boolean
    get() = this is NullOutputStream
