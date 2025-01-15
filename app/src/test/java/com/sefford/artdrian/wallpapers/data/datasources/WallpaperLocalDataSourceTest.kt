package com.sefford.artdrian.wallpapers.data.datasources

import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.test.FakeWallpaperDao
import com.sefford.artdrian.test.InjectableTest
import com.sefford.artdrian.test.mothers.WallpaperDtoMother
import com.sefford.artdrian.test.mothers.WallpaperMother
import com.sefford.artdrian.wallpapers.data.db.WallpaperDao
import com.sefford.artdrian.wallpapers.domain.model.Wallpaper
import com.sefford.artdrian.wallpapers.domain.model.WallpaperList
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
class WallpaperLocalDataSourceTest : InjectableTest() {

    @Inject
    internal lateinit var db: WallpaperDao

    @Before
    override fun beforeEach() {
        super.beforeEach()
        graph.inject(this)
    }

    @Test
    fun `retrieves the payload`() = runTest {
        db.add(WallpaperDtoMother.createWallpaper())

        WallpaperLocalDataSource(db).getMetadata().first().should { response ->
            response.shouldBeRight()
            response.value.shouldBeInstanceOf<WallpaperList>()
            (response.value as WallpaperList).wallpapers.shouldHaveSize(1)
        }
    }

    @Test
    fun `informs the cache is empty`() = runTest {
        WallpaperLocalDataSource(db).getMetadata().first().should { response ->
            response.shouldBeLeft()
            response.value.shouldBeInstanceOf<DataError.Local.Empty>()
        }
    }

    @Test
    fun `handles mapping errors`() = runTest {
        WallpaperLocalDataSource(FakeWallpaperDao(
            getAllBehavior = { flow { throw IllegalStateException() } }
        )).getMetadata().first().should { response ->
            response.shouldBeLeft()
            response.value.shouldBeInstanceOf<DataError.Local.Critical>()
        }
    }

    @Test
    fun `handles critical errors`() = runTest {
        WallpaperLocalDataSource(FakeWallpaperDao(
            getAllBehavior = { throw IllegalStateException() }
        )).getMetadata().first().should { response ->
            response.shouldBeLeft()
            response.value.shouldBeInstanceOf<DataError.Local.Critical>()
        }
    }

    @Test
    fun `retrieves a single wallpaper`() = runTest {
        db.add(WallpaperDtoMother.createWallpaper())

        WallpaperLocalDataSource(db).getMetadata(WALLPAPER_ID).first().should { response ->
            response.shouldBeRight()
            response.value.shouldBeInstanceOf<Wallpaper>()
        }
    }

    @Test
    fun `informs a wallpaper is not found`() = runTest {
        WallpaperLocalDataSource(db).getMetadata(WALLPAPER_ID).first().should { response ->
            response.shouldBeLeft()
            response.value.shouldBeInstanceOf<DataError.Local.NotFound>()
        }
    }

    @Test
    fun `saves a wallpaper`() = runTest {
        val dataSource = WallpaperLocalDataSource(db)

        dataSource.save(WallpaperMother.generateNetwork())

        dataSource.getMetadata(WALLPAPER_ID).first().should { response ->
            response.shouldBeRight()
            response.value.shouldBeInstanceOf<Wallpaper>()
        }
    }

    @Test
    fun `deletes a wallpaper`() = runTest {
        val dataSource = WallpaperLocalDataSource(db)

        dataSource.save(WallpaperMother.generateNetwork())
        dataSource.delete(WALLPAPER_ID)

        dataSource.getMetadata(WALLPAPER_ID).first().should { response ->
            response.shouldBeLeft()
            response.value.shouldBeInstanceOf<DataError.Local.NotFound>()
        }
    }

    @Test
    fun `clears the database`() = runTest {
        val dataSource = WallpaperLocalDataSource(db)

        dataSource.save(listOf(WallpaperMother.generateNetwork(), WallpaperMother.generateNetwork(id = "2")))
        dataSource.clear()

        dataSource.getMetadata().first().should { response ->
            response.shouldBeLeft()
            response.value.shouldBeInstanceOf<DataError.Local.Empty>()
        }
    }
}

private const val WALLPAPER_ID = "1"
