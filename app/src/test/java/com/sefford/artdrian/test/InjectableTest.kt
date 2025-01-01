package com.sefford.artdrian.test

import com.sefford.artdrian.di.CoreModule
import com.sefford.artdrian.di.DaggerTestComponent
import com.sefford.artdrian.di.DoublesModule
import com.sefford.artdrian.di.TestComponent
import org.junit.jupiter.api.BeforeEach

abstract class InjectableTest {

    protected lateinit var graph: TestComponent

    @BeforeEach
    open fun beforeEach() {
        graph = DaggerTestComponent.builder()
            .coreModule(CoreModule())
            .doublesModule(DoublesModule())
            .build()
    }
}
