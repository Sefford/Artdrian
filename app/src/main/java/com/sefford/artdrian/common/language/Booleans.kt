package com.sefford.artdrian.common.language

fun Boolean?.orFalse() = this ?: false

fun Boolean?.orTrue() = this ?: true
