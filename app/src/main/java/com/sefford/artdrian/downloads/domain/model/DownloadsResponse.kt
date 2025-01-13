package com.sefford.artdrian.downloads.domain.model

import arrow.core.Either
import com.sefford.artdrian.common.data.DataError

typealias DownloadsResponse = Either<DataError, Downloads>
