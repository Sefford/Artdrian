package com.sefford.artdrian.test

import com.sefford.artdrian.data.db.WallpaperDao
import com.sefford.artdrian.data.dto.WallpaperDatabaseDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeWallpaperDao(
    private val addBehavior: (Array<out WallpaperDatabaseDto>) -> Unit = {},
    private val getAllBehavior: () -> Flow<List<WallpaperDatabaseDto>> = { flowOf(emptyList()) },
    private val getSingleBehavior: (String) -> Flow<WallpaperDatabaseDto> = { flowOf() },
    private val deleteBehavior: (String) -> Unit = {},
    private val clearBehavior: () -> Unit = {}
) : WallpaperDao {

    override fun add(vararg wallpapers: WallpaperDatabaseDto) = addBehavior(wallpapers)

    override fun getAll(): Flow<List<WallpaperDatabaseDto>> = getAllBehavior()

    override fun get(id: String): Flow<WallpaperDatabaseDto> = getSingleBehavior(id)

    override fun delete(id: String) = deleteBehavior(id)

    override fun clear() = clearBehavior()
}
