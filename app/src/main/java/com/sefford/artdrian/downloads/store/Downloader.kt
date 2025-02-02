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
import com.sefford.artdrian.common.language.coroutines.DistinctChannel
import com.sefford.artdrian.connectivity.Connectivity
import com.sefford.artdrian.connectivity.ConnectivityStore
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

@OptIn(ExperimentalCoroutinesApi::class)
class Downloader(
    private val running: StateFlow<List<WorkInfo>>,
    finished: Flow<List<WorkInfo>>,
    private val connectivity: StateFlow<Connectivity>,
    private val enqueue: (OneTimeWorkRequest) -> Unit,
    private val clearNotification: () -> Unit,
    private val log: (String, String) -> Unit,
    private val scope: CoroutineScope = MainScope().plus(Dispatchers.Default)
) {

    constructor(
        workManager: WorkManager,
        connectivity: ConnectivityStore,
        clearNotification: () -> Unit,
        log: (String, String) -> Unit,
        scope: CoroutineScope = MainScope().plus(Dispatchers.Default)
    ) : this(
        workManager.getWorkInfosFlow(RUNNING_JOBS).stateIn(scope, SharingStarted.Eagerly, emptyList()),
        workManager.getWorkInfosFlow(FINISHED_JOBS),
        connectivity.state,
        workManager::enqueue,
        clearNotification,
        log,
        scope
    )

    private val channel = DistinctChannel(
        delegate = Channel<String>(Channel.UNLIMITED),
        transform = { url -> url }
    )
    private val concurrent: Int
        get() = running.value.size
    private val maxParallelization: Int
        get() = connectivity.value.speed.parallelDownloads

    init {
        finished.onEach { done ->
            log(TAG, "Finished task: Executing: $concurrent In Channel?: ${channel.isEmpty} Finished: ${done.size}")
            if (concurrent == 0 && channel.isEmpty) {
                clearNotification()
            }
            consumeAttempt()
        }.launchIn(scope)
        connectivity.map { connection -> connection is Connectivity.Connected }
            .filter { it }
            .onEach { consumeAttempt() }
            .launchIn(scope)
    }

    fun queue(url: String) = scope.launch {
        channel.send(url)
        consumeAttempt()
    }

    private suspend fun consumeAttempt() {
        if (concurrent < maxParallelization && !channel.isEmpty) {
            val url = channel.receive()
            if (!url.isDownloading) {
                log(TAG, "Starting ${url}")
                startDownload(url)
            }
        }
    }

    private val String.isDownloading: Boolean
        get() = running.value.any { task -> task.tags.contains(this) }

    private fun startDownload(url: String) {
        enqueue(
            OneTimeWorkRequestBuilder<DownloadTask>()
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .setInputData(Data.Builder().putString(DownloadTask.URL, url).build())
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
                .addTag(url)
                .build()
        )
    }
}

private val RUNNING_JOBS = WorkQuery.fromStates(WorkInfo.State.ENQUEUED, WorkInfo.State.RUNNING)
private val FINISHED_JOBS = WorkQuery.fromStates(WorkInfo.State.FAILED, WorkInfo.State.SUCCEEDED)
private val TAG = "Downloader"
