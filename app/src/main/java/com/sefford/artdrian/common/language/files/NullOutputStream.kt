package com.sefford.artdrian.common.language.files

import java.io.IOException
import java.io.OutputStream
import java.util.Objects

/** Port from Java 9 */
class NullOutputStream(private var closed: Boolean = true): OutputStream() {

    @Throws(IOException::class)
    private fun ensureOpen() {
        if (closed) {
            throw IOException("Stream closed")
        }
    }

    @Throws(IOException::class)
    override fun write(b: Int) {
        ensureOpen()
    }

    @Throws(IOException::class)
    override fun write(b: ByteArray, off: Int, len: Int) {
        Objects.checkFromIndexSize(off, len, b.size)
        ensureOpen()
    }

    override fun close() {
        closed = true
    }


}
