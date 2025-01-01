package com.sefford.artdrian.model

enum class Source(val local: Boolean, val network: Boolean) {
    NETWORK(local = false, network = true),
    LOCAL(local = true, network = false);
}
