package com.sefford.artdrian.common.language.units

import com.sefford.artdrian.common.language.isSingleUnit
import com.sefford.artdrian.common.language.toString
import kotlin.math.pow

enum class SizeUnit(private val scale: Long, private val symbol: String) {
    BYTE(1L, "bytes"),
    KILOBYTE(SCALE, "kBs"),
    MEGABYTE(SCALE.toDouble().pow(2).toLong(), "MBs"),
    GIGABYTE(SCALE.toDouble().pow(3).toLong(), "GBs"),
    PETABYTE(SCALE.toDouble().pow(4).toLong(), "PBs");

    fun format(bytes: Double) = "${bytes.toString(1)} ${if (bytes.isSingleUnit()) symbol.dropLast(1) else symbol}"

    fun asWhole(bytes: Long) = bytes / scale

    fun toBytes(size: Double): Long = (size * scale).toLong()

    fun toDouble(bytes: Long): Double = bytes / scale.toDouble()

}

private const val SCALE = 1_024L
