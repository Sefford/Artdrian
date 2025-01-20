package com.sefford.artdrian.test.mothers

import com.sefford.artdrian.common.language.files.Size.Companion.bytes
import com.sefford.artdrian.downloads.domain.model.Download
import java.io.File
import java.nio.file.Files

object DownloadsMother {

    fun createPending(
        id: String = "1",
        url: String = "http://example.com/image.jpg",
    ) = Download.Pending(id = id, url = url)

    fun createOngoing(
        id: String = "1",
        url: String = "http://example.com/image.jpg",
        hash: String = "1234",
        name: String = "ghost_waves_004.jpg",
        total: Long = 1000,
        file: File = Files.createTempFile(name, ".download").toFile(),
    ) = Download.Ongoing(
        id = id,
        url = url,
        hash = hash,
        name = name,
        total = total.bytes,
        file = file
    )

    fun createFinished(
        id: String = "1",
        url: String = "http://example.com/image.jpg",
        hash: String = "1234",
        name: String = "ghost_waves_004.jpg",
        total: Long = 1000,
        file: File = Files.createTempFile(name, ".download").toFile(),
    ) = Download.Finished(
        id = id, url = url, hash = hash, name = name, total = total.bytes, file = file
    )
}
