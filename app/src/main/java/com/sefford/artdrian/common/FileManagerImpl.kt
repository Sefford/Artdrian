package com.sefford.artdrian.common

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.sefford.artdrian.di.Application
import com.sefford.artdrian.utils.getUriFromPath
import com.sefford.artdrian.utils.isAtLeastAPI
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FileManagerImpl @Inject constructor(
    @Application private val context: Context,
    private val client: OkHttpClient
) : FileManager {

    override suspend fun saveFileIntoDirectory(source: String, target: String): String {
        return if (isAtLeastAPI(Build.VERSION_CODES.Q)) {
            saveToMediaStorage(source, target)
        } else {
            saveToExternalStorage(source, target)
        }
    }

    private fun fileExists(target: String): Boolean =
        context.contentResolver.getUriFromPath(target)

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveToMediaStorage(source: String, target: String): String {
        val absolutePathOfTarget = "file://" + Environment.getExternalStorageDirectory().absolutePath + "/" + target
        if (fileExists(target.substringAfterLast("/"))) {
            return absolutePathOfTarget
        }
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, target.substringAfterLast("/"))
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        contentValues.put(
            MediaStore.MediaColumns.RELATIVE_PATH,
            target.substringBeforeLast("/")
        )

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        if (uri != null) {
            URL(source).openStream().use { input ->
                resolver.openOutputStream(uri).use { output ->
                    input.copyTo(output!!, DEFAULT_BUFFER_SIZE)
                }
            }
        }
        return absolutePathOfTarget
    }

    private fun saveToExternalStorage(source: String, target: String): String {
        client.newCall(
            Request.Builder().url(source).build()
        ).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val folder = File(Environment.getExternalStorageDirectory(), target.substringBeforeLast("/"))
            folder.mkdirs()
            val output = File(folder, target.substringAfterLast("/"))
            output.createNewFile()
            output.writeBytes(
                response
                    .body!!
                    .byteStream()
                    .readBytes()
            )
            context.sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    output.toUri()
                )
            )
            return output.toUri().toString()
        }
    }
}
