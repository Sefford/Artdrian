package com.sefford.artdrian.test.idlingresources

import android.util.Log
import androidx.compose.ui.test.IdlingResource
import java.util.concurrent.CountDownLatch

class CountdownIdlingResource(private var counter: Int): IdlingResource {

    fun decrement() {
        counter--
    }

    override val isIdleNow: Boolean
        get() {
            return counter == 0
        }
}
