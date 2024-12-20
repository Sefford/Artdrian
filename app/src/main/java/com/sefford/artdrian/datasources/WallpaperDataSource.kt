package com.sefford.artdrian.datasources

import arrow.core.Either
import com.sefford.artdrian.datasources.WallpaperRepository.RepositoryError
import com.sefford.artdrian.model.Metadata

interface WallpaperDataSource {

    suspend fun getAllMetadata(): Either<RepositoryError, List<Metadata>>

    suspend fun saveMetadata(metadataDtoList: List<Metadata>)

    suspend fun getWallpaperMetadata(id: String): Either<RepositoryError, Metadata>

}
