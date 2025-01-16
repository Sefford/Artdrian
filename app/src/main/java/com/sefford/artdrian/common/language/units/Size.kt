package com.sefford.artdrian.common.language.units

class Size(private val size: Double, private val unit: SizeUnit) {

    val inBytes: Long by lazy { unit.toBytes(size) }
    val inWholekBs: Long by lazy { SizeUnit.KILOBYTE.asWhole(inBytes) }
    val inWholeMBs: Long by lazy { SizeUnit.MEGABYTE.asWhole(inBytes) }
    val inWholeGBs: Long by lazy { SizeUnit.GIGABYTE.asWhole(inBytes) }
    val inWholePBs: Long by lazy { SizeUnit.PETABYTE.asWhole(inBytes) }

    private val readable: String by lazy {
        SizeUnit.entries.asSequence()
            .map { unit -> toDouble(unit) to unit }
            .last { (size, _) -> size >= 1 }
            .let { (size, unit) -> unit.format(size) }
    }

    private fun toDouble(size: SizeUnit) = size.toDouble(inBytes)

    operator fun plus(downloaded: Long): Size = Size(inBytes + downloaded.toDouble(), SizeUnit.BYTE)

    operator fun plus(size: Size): Size = Size(inBytes + size.size, SizeUnit.BYTE)

    override fun toString(): String = readable

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Size) return false

        if (inBytes != other.inBytes) return false

        return true
    }

    override fun hashCode(): Int {
        return inBytes.hashCode()
    }

    operator fun compareTo(progress: Size): Int = inBytes.compareTo(progress.inBytes)

    operator fun compareTo(progress: Int): Int = inBytes.compareTo(progress)

    companion object {
        inline val Number.bytes: Size get() = Size(this.toDouble(), SizeUnit.BYTE)
        inline val Number.kBs: Size get() = Size(this.toDouble(), SizeUnit.KILOBYTE)
        inline val Number.MBs: Size get() = Size(this.toDouble(), SizeUnit.MEGABYTE)
        inline val Number.GBs: Size get() = Size(this.toDouble(), SizeUnit.GIGABYTE)
        inline val Number.PBs: Size get() = Size(this.toDouble(), SizeUnit.PETABYTE)
    }
}
