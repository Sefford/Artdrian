package com.sefford.artdrian.datasources

import arrow.core.Either
import com.sefford.artdrian.datasources.WallpaperRepository.RepositoryError
import com.sefford.artdrian.data.dto.MetadataDto

interface WallpaperDataSource {

    suspend fun getAllMetadata(): Either<RepositoryError, List<MetadataDto>>

    suspend fun saveMetadata(metadataDtoList: List<MetadataDto>)

    suspend fun getWallpaperMetadata(id: String): Either<RepositoryError, MetadataDto>

}
