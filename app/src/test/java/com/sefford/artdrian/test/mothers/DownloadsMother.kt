package com.sefford.artdrian.test.mothers

import com.sefford.artdrian.common.language.files.Size.Companion.bytes
import com.sefford.artdrian.downloads.domain.model.Download
import java.io.File
import java.nio.file.Files

object DownloadsMother {

    fun createPending(
        url: String = "http://example.com/mobile/image.jpg",
    ) = Download.Pending(url = url)

    fun createOngoing(
        url: String = "http://example.com/mobile/image.jpg",
        hash: String = "1234",
        name: String = "ghost_waves_004.jpg",
        total: Long = 1000,
        file: File = Files.createTempFile(name, ".${Download.Format(url).suffix}.download").toFile(),
    ) = Download.Ongoing(
        url = url,
        hash = hash,
        name = name,
        total = total.bytes,
        file = file
    )

    fun createFinished(
        url: String = "http://example.com/mobile/image.jpg",
        hash: String = "1234",
        name: String = "ghost_waves_004.jpg",
        total: Long = 1000,
        file: File = Files.createTempFile(name, ".${Download.Format(url).suffix}.download").toFile(),
    ) = Download.Finished(
        url = url,
        hash = hash,
        name = name,
        total = total.bytes,
        file = file
    )
}
