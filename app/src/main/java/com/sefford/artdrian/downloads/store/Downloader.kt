package com.sefford.artdrian.downloads.store

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import com.sefford.artdrian.connectivity.Connectivity
import com.sefford.artdrian.connectivity.ConnectivityStore
import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.downloads.tasks.DownloadTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import java.util.concurrent.TimeUnit

class Downloader(
    private val running: StateFlow<List<WorkInfo>>,
    finished: Flow<List<WorkInfo>>,
    private val connectivity: StateFlow<Connectivity>,
    private val enqueue: (OneTimeWorkRequest) -> Unit,
    private val scope: CoroutineScope = MainScope().plus(Dispatchers.Default)
) {

    constructor(
        workManager: WorkManager,
        connectivity: ConnectivityStore,
        scope: CoroutineScope = MainScope().plus(Dispatchers.Default)
    ) : this(
        workManager.getWorkInfosFlow(RUNNING_JOBS).stateIn(scope, SharingStarted.Lazily, emptyList()),
        workManager.getWorkInfosFlow(FINISHED_JOBS),
        connectivity.state,
        workManager::enqueue,
        scope
    )

    private val channel = Channel<Download>(Channel.UNLIMITED)
    private val concurrent: Int
        get() = running.value.size
    private val maxParallelization: Int
        get() = connectivity.value.speed.parallelDownloads

    init {
        finished.onEach {
            consumeAttempt()
        }.launchIn(scope)
        connectivity.map { connection -> connection is Connectivity.Connected }
            .filter { it }
            .onEach { consumeAttempt() }
            .launchIn(scope)
    }

    fun queue(download: Download) = scope.launch {
        channel.send(download)
        consumeAttempt()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun consumeAttempt() {
        if (concurrent < maxParallelization && !channel.isEmpty) {
            val download = channel.receive()
            if (!download.isDownloading) {
                startDownload(download)
            }
        }
    }

    private val Download.isDownloading: Boolean
        get() = running.value.find { task -> task.tags.contains(id) } != null

    private fun startDownload(download: Download) {
        enqueue(
            OneTimeWorkRequestBuilder<DownloadTask>()
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .setInputData(Data.Builder().putString(DownloadTask.ID, download.id).build())
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
                .addTag(download.id)
                .build()
        )
    }
}

private val RUNNING_JOBS = WorkQuery.fromStates(WorkInfo.State.ENQUEUED, WorkInfo.State.RUNNING)
private val FINISHED_JOBS = WorkQuery.fromStates(WorkInfo.State.FAILED, WorkInfo.State.SUCCEEDED)
