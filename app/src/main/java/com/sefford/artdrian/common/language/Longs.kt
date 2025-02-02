package com.sefford.artdrian.common.language

fun Long.percentage(total: Long): Float = if (total == 0L) 0f else (toFloat() / total) * 100
