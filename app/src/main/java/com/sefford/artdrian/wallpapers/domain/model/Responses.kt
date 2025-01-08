package com.sefford.artdrian.wallpapers.domain.model

import arrow.core.Either
import com.sefford.artdrian.common.data.DataError

typealias MetadataResponse = Either<DataError, WallpaperList>

typealias SingleMetadataResponse = Either<DataError, Wallpaper>

