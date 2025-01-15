package com.sefford.artdrian.common.language

fun <T> Iterable<T>.intersect(
    other: Iterable<T>,
    equality: (T, T) -> Boolean,
    merge: (T, T) -> T
): Set<T> = buildSet {
    this@intersect.forEach { left ->
        other.forEach { right ->
            if (equality(left, right)) {
                add(merge(left, right))
            }
        }
    }
}

fun <T> Iterable<T>.difference(
    other: Iterable<T>,
    equality: (T, T) -> Boolean,
): List<T> =
    buildList {
        addAll(this@difference.filter { left -> other.none { right -> equality(left, right) } })
        addAll(other.filter { right -> this@difference.none { left -> equality(left, right) } })
    }
