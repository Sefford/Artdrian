package com.sefford.artdrian.downloads.data.datasources

import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.downloads.db.DownloadsDao
import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.downloads.domain.model.Downloads
import com.sefford.artdrian.test.FakeDownloadsDao
import com.sefford.artdrian.test.InjectableTest
import com.sefford.artdrian.test.mothers.DownloadsDtoMother
import com.sefford.artdrian.test.mothers.DownloadsMother
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class DownloadsDataSourceTest : InjectableTest() {

    @Inject
    internal lateinit var db: DownloadsDao

    @Before
    override fun beforeEach() {
        super.beforeEach()
        graph.inject(this)
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `retrieves the downloads`() = runTest {
        db.add(DownloadsDtoMother.create(), DownloadsDtoMother.create("2"), DownloadsDtoMother.create("3"))

        DownloadsDataSource(db).getAll().first().should { response ->
            response.shouldBeRight()
            response.value.shouldBeInstanceOf<Downloads>()
            (response.value as Downloads).filterIsInstance<Download.Pending>().shouldHaveSize(3)
        }
    }

    @Test
    fun `informs the cache is empty`() = runTest {
        DownloadsDataSource(db).getAll().first().should { response ->
            response.shouldBeLeft()
            response.value.shouldBeInstanceOf<DataError.Local.Empty>()
        }
    }

    @Test
    fun `handles mapping errors`() = runTest {
        DownloadsDataSource(FakeDownloadsDao(
            getAllBehavior = { flow { throw IllegalStateException() } }
        )).getAll().first().should { response ->
            response.shouldBeLeft()
            response.value.shouldBeInstanceOf<DataError.Local.Critical>()
        }
    }

    @Test
    fun `handles critical errors`() = runTest {
        DownloadsDataSource(FakeDownloadsDao(
            getAllBehavior = { throw IllegalStateException() }
        )).getAll().first().should { response ->
            response.shouldBeLeft()
            response.value.shouldBeInstanceOf<DataError.Local.Critical>()
        }
    }

    @Test
    fun `saves a wallpaper`() = runTest {
        val dataSource = DownloadsDataSource(db)

        dataSource.save(setOf(DownloadsMother.createPending()))

        dataSource.getAll().first().should { response ->
            response.shouldBeRight()
            response.value.shouldBeInstanceOf<Downloads>()
            @Suppress("UNCHECKED_CAST")
            (response.value as Downloads).shouldHaveSize(1)
        }
    }
}
