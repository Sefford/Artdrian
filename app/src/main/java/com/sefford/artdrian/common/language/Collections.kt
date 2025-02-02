package com.sefford.artdrian.common.language

typealias Matcher<T> = T.(T) -> Boolean
typealias Merger<T> = T.(T) -> T

fun <T> Iterable<T>.intersect(
    other: Iterable<T>,
    equalTo: Matcher<T> = ::id,
    mergeWith: Merger<T> = ::mergeRight
): Set<T> = buildSet {
    this@intersect.forEach { left ->
        other.forEach { right ->
            if (left.equalTo(right)) {
                add(left.mergeWith(right))
            }
        }
    }
}

fun <T> Iterable<T>.outerJoin(
    other: Iterable<T>,
    equality: Matcher<T> = ::id,
): List<T> =
    buildList {
        addAll(this@outerJoin.filter { left -> other.none { right -> equality(left, right) } })
        addAll(other.filter { right -> this@outerJoin.none { left -> equality(left, right) } })
    }

fun <T> Iterable<T>.minus(other: Iterable<T>, equality: Matcher<T>): List<T> =
    filter { left -> other.none { right -> equality(left, right) } }

operator fun <T> Iterable<T>.minus(other: Iterable<T>) = minus(other, ::id)

private fun <T> id(left: T, right: T) = left == right

fun<T> mergeLeft(left: T, right:T) = left

fun<T> mergeRight(left: T, right:T) = right

