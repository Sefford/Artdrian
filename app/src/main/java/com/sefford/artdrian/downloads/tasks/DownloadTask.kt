package com.sefford.artdrian.downloads.tasks

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import arrow.core.flatMap
import com.sefford.artdrian.common.data.network.HttpClient
import com.sefford.artdrian.common.di.Downloads
import com.sefford.artdrian.common.language.fold
import com.sefford.artdrian.common.utils.Logger
import com.sefford.artdrian.common.utils.graph
import com.sefford.artdrian.downloads.domain.model.DownloadProcess
import com.sefford.artdrian.downloads.domain.model.DownloadProcess.Results.Failure
import com.sefford.artdrian.downloads.domain.model.DownloadProcess.Results.Retry
import com.sefford.artdrian.downloads.domain.model.DownloadProcess.Results.Success
import com.sefford.artdrian.downloads.store.DownloadsStore
import java.io.File
import javax.inject.Inject


class DownloadTask(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    @Inject
    internal lateinit var client: HttpClient

    @Inject
    internal lateinit var downloads: DownloadsStore

    @Inject
    @Downloads
    internal lateinit var cache: File

    @Inject
    internal lateinit var logger: Logger

    override suspend fun doWork(): Result {
        graph.inject(this)

        return DownloadProcess.Step.Viability(client, downloads, cache, logger, inputData.getString(URL)!!)
            .check()
            .flatMap { probe -> probe.analyze() }
            .flatMap { prime -> prime.ready() }
            .map { fetch -> fetch.start() }
            .fold { result -> result.operation }
    }

    val DownloadProcess.Results.operation: Result
        get() = when (this) {
            Failure -> Result.failure()
            Retry -> Result.retry()
            Success -> Result.success()
        }

    companion object {
        const val URL = "id"
    }
}
