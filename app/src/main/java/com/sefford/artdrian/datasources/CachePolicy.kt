package com.sefford.artdrian.datasources

import arrow.core.Either
import com.sefford.artdrian.model.Metadata

enum class CachePolicy() {
    PRIORITIZE_LOCAL,
    PRIORITIZE_NETWORK,
    NETWORK_ONLY,
    OFFLINE
}
