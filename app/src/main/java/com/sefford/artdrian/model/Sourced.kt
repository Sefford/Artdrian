package com.sefford.artdrian.model

interface Sourced {
    val local: Boolean
    val network: Boolean
    val source: Sourced
        get() = this

    object Local: Sourced {
        override val local: Boolean = true
        override val network: Boolean = false

        override fun toString(): String {
            return "Source: Local"
        }
    }

    object Network: Sourced {
        override val local: Boolean = false
        override val network: Boolean = true

        override fun toString(): String {
            return "Source: Network"
        }
    }
}



