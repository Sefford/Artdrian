package com.sefford.artdrian.test.mothers

import com.sefford.artdrian.common.language.files.Size.Companion.bytes
import com.sefford.artdrian.downloads.domain.model.Download

object DownloadsMother {

    fun createPending(
        id: String = "1",
        url: String = "http://example.com/image.jpg",
    ) = Download.Pending(id = id, url = url)

    fun createPrimed(
        id: String = "1",
        url: String = "http://example.com/image.jpg",
        name: String = "ghost_waves.jpg",
        hash: String = "1234",
        total: Long = 1000,
    ) = Download.Primed(id = id, url = url, hash = hash, name = name, total = total.bytes)

    fun createOngoing(
        id: String = "1",
        url: String = "http://example.com/image.jpg",
        hash: String = "1234",
        name: String = "ghost_waves.jpg",
        total: Long = 1000,
        downloaded: Long = 250,
        uri: String = "file://target/1234",
    ) = Download.Ongoing(
        id = id,
        url = url,
        hash = hash,
        name = name,
        total = total.bytes,
        progress = downloaded.bytes,
        uri = uri
    )

    fun createFinished(
        id: String = "1",
        url: String = "http://example.com/image.jpg",
        hash: String = "1234",
        name: String = "ghost_waves.jpg",
        total: Long = 1000,
        uri: String = "file://target/1234",
    ) = Download.Finished(id = id, url = url, hash = hash, name = name, total = total.bytes, uri = uri)
}
