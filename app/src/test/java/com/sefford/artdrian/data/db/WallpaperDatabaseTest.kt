package com.sefford.artdrian.data.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.test.mothers.DatabaseDtoMother
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class WallpaperDatabaseTest {
    private lateinit var database: WallpaperDatabase
    private lateinit var dao: WallpaperDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WallpaperDatabase::class.java
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
        dao.add(DatabaseDtoMother.createWallpaper())

        dao.getAll().first().shouldHaveSize(1)
    }

    @Test
    fun `retrieves all elements`() = runTest {
        dao.add(DatabaseDtoMother.createWallpaper(), DatabaseDtoMother.createWallpaper("2"))

        dao.getAll().first().shouldHaveSize(2)
    }

    @Test
    fun `retrieves an element`() = runTest {
        dao.add(DatabaseDtoMother.createWallpaper(), DatabaseDtoMother.createWallpaper("2"))

        dao.get("1").first().matchWithSnapshot()
    }

    @Test
    fun `deletes an element`() = runTest {
        dao.add(DatabaseDtoMother.createWallpaper(), DatabaseDtoMother.createWallpaper("2"))

        dao.delete("1")

        dao.getAll().first().shouldHaveSize(1)
    }

    @Test
    fun `clears the database`() = runTest {
        dao.add(DatabaseDtoMother.createWallpaper(), DatabaseDtoMother.createWallpaper("2"))

        dao.clear()

        dao.getAll().first().shouldBeEmpty()
    }
}
