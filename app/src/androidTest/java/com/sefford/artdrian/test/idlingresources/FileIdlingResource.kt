package com.sefford.artdrian.test.idlingresources

import androidx.compose.ui.test.IdlingResource
import java.io.File

class FileIdlingResource(file: String) : IdlingResource {

    private val file: File = File(file)

    override val isIdleNow: Boolean
        get() = file.exists()
}
