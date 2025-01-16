package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.common.language.files.Size

interface Measured {

    val total: Size

    val progress: Size
}
