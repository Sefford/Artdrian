package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.common.language.difference
import com.sefford.artdrian.common.language.intersect

typealias Downloads = List<Download>

@Suppress("USELESS_CAST")
operator fun Downloads.plus(other: Downloads): Downloads {
    val equality: (Download, Download) -> Boolean = { left, right -> left as Download == right as Download }
    return (this.intersect(other, equality) { left, right -> left + right } +
        this.difference(other, equality)).toList()
}
