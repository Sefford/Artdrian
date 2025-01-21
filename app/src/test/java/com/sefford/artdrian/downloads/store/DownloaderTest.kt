package com.sefford.artdrian.downloads.store

import androidx.work.WorkInfo
import com.sefford.artdrian.connectivity.Connectivity
import com.sefford.artdrian.test.FakeEnqueuer
import com.sefford.artdrian.test.mothers.DownloadsMother
import com.sefford.artdrian.test.mothers.WorkInfoMother
import com.sefford.artdrian.test.unconfine
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class DownloaderTest {

    private val runningJobs = MutableStateFlow<List<WorkInfo>>(emptyList())
    private val finishedJobs = MutableSharedFlow<List<WorkInfo>>()
    private val connectivity = MutableStateFlow<Connectivity>(Connectivity.Disconnected)
    private val enqueuer = FakeEnqueuer()

    @Test
    fun `holds the queue while there is no connection`() = runTest {
        givenADownloader().queue(DownloadsMother.createPending())

        enqueuer.queue.shouldBeEmpty()
    }

    @Test
    fun `does nothing when reconnecting with an empty channel`() = runTest {
        givenADownloader()

        connectivity.emit(WIFI)

        enqueuer.queue.shouldBeEmpty()
    }

    @Test
    fun `discards downloads that are already ongoing`() = runTest {
        connectivity.emit(WIFI)
        runningJobs.emit(listOf(WorkInfoMother.createWorkInfo(tags = setOf("1"))))

        givenADownloader().queue(DownloadsMother.createPending())

        enqueuer.queue.shouldBeEmpty()
    }

    @Test
    fun `enqueues a job`() = runTest {
        connectivity.emit(WIFI)

        givenADownloader().queue(DownloadsMother.createPending())

        enqueuer.queue.shouldHaveSize(1)
    }

    @Test
    fun `enqueues next job on connecting`() = runTest {
        givenADownloader().queue(DownloadsMother.createPending())

        connectivity.emit(WIFI)

        enqueuer.queue.shouldHaveSize(1)
    }

    @Test
    fun `enqueues next job on finish`() = runTest {
        // The connection is throttled
        connectivity.emit(THROTTLED)

        // We enqueue a task manually
        val task = WorkInfoMother.createWorkInfo()
        runningJobs.emit(listOf(task))

        val downloader = givenADownloader()

        // We queue a task
        downloader.queue(DownloadsMother.createPending())

        // This task should not be queued because we reach max parallelization
        enqueuer.queue.shouldBeEmpty()

        // We mark job as done
        runningJobs.emit(emptyList())
        finishedJobs.emit(listOf(task))

        enqueuer.queue.shouldHaveSize(1)
    }

    private fun TestScope.givenADownloader() = Downloader(
        runningJobs,
        finishedJobs,
        connectivity,
        enqueuer::enqueue,
        backgroundScope.unconfine()
    )
}

private val WIFI = Connectivity.Connected(Connectivity.Type.WIFI, Connectivity.Speed.Fast(56))
private val THROTTLED = Connectivity.Connected(Connectivity.Type.MOBILE, Connectivity.Speed.Slow(0))
