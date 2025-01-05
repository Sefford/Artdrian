package com.sefford.artdrian.language

fun Boolean?.orFalse() = this ?: false

fun Boolean?.orTrue() = this ?: true
