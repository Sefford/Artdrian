package com.sefford.artdrian.common.stores

import com.sefford.artdrian.common.utils.Logger

class LoggableEventProcessor<Event>(
    private val delegate: ReceivesEvents<Event>,
    private val logger: Logger,
    private val tag: String
): ReceivesEvents<Event> {

    constructor(pair: Pair<ReceivesEvents<Event>, Logger>, tag: String) : this(pair.first, pair.second, tag)

    constructor(pair: Pair<ReceivesEvents<Event>, String>, logger: Logger) : this(pair.first, logger, pair.second)

    override fun event(event: Event) {
        logger.debug(tag, "Received Event: $event")
        delegate.event(event)
    }
}

operator fun <Event> ReceivesEvents<Event>.plus(logger: Logger) = this to logger

operator fun <Event> ReceivesEvents<Event>.plus(tag: String) = this to tag

operator fun <Event> Pair<ReceivesEvents<Event>, Logger>.plus(tag: String) = LoggableEventProcessor(this, tag)

operator fun <Event> Pair<ReceivesEvents<Event>, String>.plus(logger: Logger) = LoggableEventProcessor(this, logger)
