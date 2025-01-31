package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.common.language.intersect
import com.sefford.artdrian.common.language.outerJoin

typealias Downloads = List<Download>

operator fun Downloads.plus(other: Downloads): Downloads {
    return (intersect(other) { that -> this + that } + outerJoin(other)).toList()
}

val Downloads.progress: Long
    get() = filterIsInstance<Measured>().sumOf { download -> download.progress.inBytes }

val Downloads.total: Long
    get() = filterIsInstance<Measured>().sumOf { download -> download.total.inBytes }

