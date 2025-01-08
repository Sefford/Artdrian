package com.sefford.artdrian.common.language

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun <A, B, C> Either<A, B>.flatMapLeft(f: (left: A) -> Either<C, B>): Either<C, B> {
    contract { callsInPlace(f, InvocationKind.AT_MOST_ONCE) }
    return when (this) {
        is Left -> f(this.value)
        is Right -> this
    }
}
