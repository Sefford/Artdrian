package com.sefford.artdrian.test.mothers

import com.sefford.artdrian.common.language.units.Size.Companion.bytes
import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.wallpapers.domain.model.Sourced

object DownloadsMother {

    fun createPending(
        id: String = "1",
        url: String = "http://example.com/image.jpg",
    ) = Download.Pending(id = id, url = url)

    fun createPrimed(
        id: String = "1",
        url: String = "http://example.com/image.jpg",
        hash: String = "1234",
        total: Long = 1000,
    ) = Download.Primed(id = id, url = url, hash = hash, total = total.bytes)

    fun createOngoing(
        id: String = "1",
        url: String = "http://example.com/image.jpg",
        hash: String = "1234",
        total: Long = 1000,
        downloaded: Long = 250,
        uri: String = "file://target/1234",
    ) = Download.Ongoing(id, url, hash, total.bytes, downloaded.bytes, uri)

    fun createFinished(
        id: String = "1",
        url: String = "http://example.com/image.jpg",
        hash: String = "1234",
        total: Long = 1000,
        uri: String = "file://target/1234",
    ) = Download.Finished(id, url, hash, total.bytes, uri)
}
