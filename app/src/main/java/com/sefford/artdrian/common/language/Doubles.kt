package com.sefford.artdrian.common.language

import java.util.Locale

fun Double.toString(decimalPlaces: Int) = if (this % 1.0 == 0.0) {
    toInt().toString()
} else {
    String.format(Locale.getDefault(), "%.${decimalPlaces}f", this) // Single decimal
}

fun Double.isSingleUnit(): Boolean = "%.1f".format(Locale.US,this).also { println(it) } == "1.0"
