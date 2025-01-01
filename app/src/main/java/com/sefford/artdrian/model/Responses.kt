package com.sefford.artdrian.model

import arrow.core.Either
import com.sefford.artdrian.data.DataError

typealias MetadataResponse = Either<DataError, WallpaperList>

typealias SingleMetadataResponse = Either<DataError, Wallpaper>

