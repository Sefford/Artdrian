package com.sefford.artdrian.test

import com.sefford.artdrian.downloads.data.dto.DownloadDto
import com.sefford.artdrian.downloads.db.DownloadsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeDownloadsDao(
    private val addBehavior: (Array<out DownloadDto>) -> Unit = {},
    private val getAllBehavior: () -> Flow<List<DownloadDto>> = { flowOf(emptyList()) },
    private val getSingleBehavior: (String) -> DownloadDto? = { null },
    private val deleteBehavior: (String) -> Unit = {},
    private val clearBehavior: () -> Unit = {}
) : DownloadsDao {

    override fun add(vararg downloads: DownloadDto) = addBehavior(downloads)

    override fun getAll(): Flow<List<DownloadDto>> = getAllBehavior()

    override suspend fun get(url: String): DownloadDto? = getSingleBehavior(url)

    override fun delete(id: String) = deleteBehavior(id)

    override fun clear() = clearBehavior()
}
