package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.common.language.files.Size

interface Measured {

    val total: Size

    val progress: Size

    val percentage: Float
        get() = if (total > 0) (progress / total) * 100 else 0f
}
