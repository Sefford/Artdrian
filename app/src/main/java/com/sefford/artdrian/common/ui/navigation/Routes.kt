package com.sefford.artdrian.common.ui.navigation

import android.net.Uri

sealed class Routes {

    data object WallpaperList : Blueprint by Simple("home")

    class WallpaperDetail(private val id: String, private val name: String) : Routes() {

        override val destination: String by lazy { template.format(Uri.encode(id), Uri.encode(name)) }

        companion object : Blueprint {
            override val template: String = "details/%s/%s"

            override val blueprint: String by lazy { template.format("{id}", "{name}") }
        }
    }

    abstract val destination: String

    interface Blueprint {
        val template: String

        val blueprint: String
    }
}

private class Simple(override val template: String) : Routes.Blueprint {
    override val blueprint: String = template
}
