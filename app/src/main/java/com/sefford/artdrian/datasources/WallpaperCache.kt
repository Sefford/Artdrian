package com.sefford.artdrian.datasources

import com.sefford.artdrian.model.Metadata

interface WallpaperCache {
    suspend fun save(metadata: List<Metadata>)

    suspend fun save(metadata: Metadata)

    suspend fun delete(id: String)

    suspend fun clear()
}
