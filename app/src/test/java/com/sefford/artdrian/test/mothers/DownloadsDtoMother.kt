package com.sefford.artdrian.test.mothers

import com.sefford.artdrian.downloads.data.dto.DownloadDto

object DownloadsDtoMother {

    fun create(
        id: String = "1",
        url: String = "http://example.com/image.jpg",
        hash: String = "",
        total: Long = 1000,
        downloaded: Long = 250,
        uri: String = "file://target/1234"
    ): DownloadDto {
        return DownloadDto(
            id = id,
            url = url,
            hash = hash,
            total = total,
            downloaded = downloaded,
            uri = uri
        )
    }

    fun createPending(
        id: String = "pending",
        url: String = "http://example.com/image.jpg",
    ) = create(id = id, url = url, hash = "", total = -1, downloaded = -1, uri = "")

    fun createPrimed(
        id: String = "primed",
        url: String = "http://example.com/image.jpg",
        hash: String = "1234",
    ) = create(id = id, url = url, hash = hash, downloaded = 0)

    fun createOngoing(
        id: String = "ongoing",
        hash: String = "1234",
        uri: String = "file://target/1234"
    ) = create(id = id, hash = hash, uri = uri)

    fun createFinished(
        id: String = "finished",
        hash: String = "1234",
        total: Long = 1000,
    ) = create(id = id, hash = hash, total = total, downloaded = total)
}
