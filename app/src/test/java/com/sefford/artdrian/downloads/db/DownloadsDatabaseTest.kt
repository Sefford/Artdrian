package com.sefford.artdrian.downloads.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.test.mothers.DownloadsDtoMother
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.collections.shouldHaveSize
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DownloadsDatabaseTest {
    private lateinit var database: DownloadsDatabase
    private lateinit var dao: DownloadsDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DownloadsDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        dao = database.dao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `adds an element`() = runTest {
        dao.add(DownloadsDtoMother.create())

        dao.getAll().first().shouldHaveSize(1)
    }

    @Test
    fun `retrieves all elements`() = runTest {
        dao.add(DownloadsDtoMother.create(), DownloadsDtoMother.create(SECOND_URL))

        dao.getAll().first().shouldHaveSize(2)
    }

    @Test
    fun `retrieves an element`() = runTest {
        dao.add(DownloadsDtoMother.create(), DownloadsDtoMother.create(SECOND_URL))

        dao.get(FIRST_URL).matchWithSnapshot()
    }

    @Test
    fun `deletes an element`() = runTest {
        dao.add(DownloadsDtoMother.create(), DownloadsDtoMother.create(SECOND_URL))

        dao.delete(FIRST_URL)

        dao.getAll().first().shouldContainOnly(DownloadsDtoMother.create(SECOND_URL))
    }

    @Test
    fun `clears the database`() = runTest {
        dao.add(DownloadsDtoMother.create(), DownloadsDtoMother.create(SECOND_URL))

        dao.clear()

        dao.getAll().first().shouldBeEmpty()
    }
}

private const val FIRST_URL = "http://example.com/mobile/image.jpg"
private const val SECOND_URL = "http://example.com/desktop/image.jpg"
