package com.sefford.artdrian.wallpapers.domain.usecases

import android.os.Environment
import arrow.core.Either
import com.sefford.artdrian.common.FileManager
import java.io.File
import javax.inject.Inject

class DownloadWallpaper @Inject constructor(
    private val fileManager: FileManager
) {

    suspend fun download(url: String): Either<PersistingError, String> {
        return Either.catch {
            fileManager.saveFileIntoDirectory(
                url,
                Environment.DIRECTORY_PICTURES + File.separator + "Wallpapers" + File.separator + "Artdrian" + File.separator +
                        url.substringAfterLast("/"))
        }.mapLeft { exception ->
            PersistingError(exception)
        }
    }

    class PersistingError(val exception: Throwable)
}
