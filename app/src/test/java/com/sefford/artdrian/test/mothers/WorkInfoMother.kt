package com.sefford.artdrian.test.mothers

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.WorkInfo
import java.util.UUID

object WorkInfoMother {

    fun createWorkInfo(
        id: UUID = UUID.randomUUID(),
        state: WorkInfo.State = WorkInfo.State.ENQUEUED,
        tags: Set<String> = emptySet(),
        output: Data = Data.EMPTY,
        progress: Data = Data.EMPTY,
        run: Int = 0,
        generation: Int = 0,
        constraints: Constraints = Constraints.NONE,
        initialDelayMillis: Long = 0,
        periodicity: WorkInfo.PeriodicityInfo? = null,
        nextScheduleTimeMillis: Long = Long.MAX_VALUE,
        stopReason: Int = WorkInfo.STOP_REASON_NOT_STOPPED
    ): WorkInfo {
        return WorkInfo(
            id = id,
            state = state,
            tags = tags,
            outputData = output,
            progress = progress,
            runAttemptCount = run,
            generation = generation,
            constraints = constraints,
            initialDelayMillis = initialDelayMillis,
            periodicityInfo = periodicity,
            nextScheduleTimeMillis = nextScheduleTimeMillis,
            stopReason = stopReason
        )
    }

}
