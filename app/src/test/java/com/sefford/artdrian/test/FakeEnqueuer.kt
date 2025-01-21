package com.sefford.artdrian.test

import androidx.work.OneTimeWorkRequest

class FakeEnqueuer(private val _queue: MutableList<OneTimeWorkRequest> = mutableListOf()) {

    val queue: List<OneTimeWorkRequest>
        get() = _queue.toList()

    fun enqueue(task: OneTimeWorkRequest) = _queue.add(task)
}
