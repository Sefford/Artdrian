package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.common.language.intersect
import com.sefford.artdrian.common.language.outerJoin
import com.sefford.artdrian.common.language.percentage

typealias Downloads = Set<Download>

operator fun Downloads.plus(other: Downloads): Downloads =
    (intersect(other) { that -> this + that } + outerJoin(other))

val Downloads.progress: Long
    get() = filterIsInstance<Measured>().sumOf { download -> download.progress.inBytes }

val Downloads.total: Long
    get() = filterIsInstance<Measured>().sumOf { download -> download.total.inBytes }

val Downloads.percentage: Float
    get() = progress.percentage(total)

fun Downloads.filterFinished() = filterNot { download -> download.finished }.toSet()
