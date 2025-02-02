package com.sefford.artdrian.test.mothers

import com.sefford.artdrian.downloads.data.dto.DownloadDto

object DownloadsDtoMother {

    fun create(
        url: String = "http://example.com/mobile/image.jpg",
        hash: String = "",
        total: Long = 1000,
        downloaded: Long = 250,
        uri: String = "file://target/1234"
    ): DownloadDto {
        return DownloadDto(
            url = url,
            hash = hash,
            total = total,
            downloaded = downloaded,
            uri = uri
        )
    }

    fun createPending(
        url: String = "http://example.com/mobile/image.jpg",
    ) = create(url = url, hash = "", total = -1, downloaded = -1, uri = "")

    fun createOngoing(
        url: String = "http://example.com/mobile/image.jpg",
        hash: String = "1234",
        uri: String = "file://target/1234"
    ) = create(url = url, hash = hash, uri = uri)

    fun createFinished(
        url: String = "http://example.com/mobile/image.jpg",
        hash: String = "1234",
        total: Long = 1000,
    ) = create(url = url, hash = hash, total = total, downloaded = total)
}
