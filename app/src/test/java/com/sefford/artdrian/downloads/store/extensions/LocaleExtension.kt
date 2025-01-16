package com.sefford.artdrian.downloads.store.extensions

import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import java.util.Locale

class LocaleExtension(
    private val locale: Locale = Locale.US
): BeforeEachCallback, AfterEachCallback {

    private val default = Locale.getDefault()

    override fun beforeEach(context: ExtensionContext?) {
        Locale.setDefault(locale)
    }

    override fun afterEach(context: ExtensionContext?) {
        Locale.setDefault(default)
    }
}
