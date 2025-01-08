package com.sefford.artdrian.wallpapers.effects

import arrow.core.left
import arrow.core.right
import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.wallpapers.domain.model.WallpaperList
import com.sefford.artdrian.test.mothers.WallpaperMother
import com.sefford.artdrian.wallpapers.store.WallpaperEffects
import com.sefford.artdrian.wallpapers.store.WallpaperEvents
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class WallpaperDomainEffectHandlerTest {

    @Test
    fun `loads the wallpapers`() = runTest {
        WallpaperDomainEffectHandler(getAllMetadata = { flowOf(ALL_METADATA_RESPONSE) }, scope = this)
            .handle(WallpaperEffects.LoadAll) { event ->
                event.shouldBeInstanceOf<WallpaperEvents.OnResponseReceived>()
            }
    }

    @Test
    fun `receives an error when requesting all the wallpapers`() = runTest {
        WallpaperDomainEffectHandler(getAllMetadata = { flowOf(ERROR) }, scope = this)
            .handle(WallpaperEffects.LoadAll) { event ->
                event.shouldBeInstanceOf<WallpaperEvents.OnErrorReceived>()
            }
    }

    @Test
    fun `loads a single wallpaper`() = runTest {
        WallpaperDomainEffectHandler(getSingleMetadata = { _ -> flowOf(SINGLE_METADATA_RESPONSE) }, scope = this)
            .handle(WallpaperEffects.LoadAll) { event ->
                event.shouldBeInstanceOf<WallpaperEvents.OnResponseReceived>()
            }
    }

    @Test
    fun `receives an error when requesting a single wallpaper`() = runTest {
        WallpaperDomainEffectHandler(getAllMetadata = { flowOf(ERROR) }, scope = this)
            .handle(WallpaperEffects.LoadAll) { event ->
                event.shouldBeInstanceOf<WallpaperEvents.OnErrorReceived>()
            }
    }
}

private val ALL_METADATA_RESPONSE = WallpaperList.FromNetwork(emptyList()).right()
private val SINGLE_METADATA_RESPONSE = WallpaperMother.generateNetwork().right()
private val ERROR = DataError.Network.Invalid(HttpStatusCode.BadRequest.value).left()
