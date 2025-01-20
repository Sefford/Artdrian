package com.sefford.artdrian.common.language

import java.io.Closeable

suspend fun <A : Closeable, B : Closeable, C> A.useWith(b: B, f: suspend (A, B) -> C): C =
    use { a ->
        b.use { b ->
            f(a, b)
        }
    }
