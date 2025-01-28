package com.sefford.artdrian.common

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import arrow.core.Either
import com.sefford.artdrian.common.di.Application
import com.sefford.artdrian.common.language.orFalse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuildConfig @Inject constructor(
    @Application private val context: Context,
    private val packageManager: PackageManager,
) {

    val debug: Boolean by lazy {
        Either.catch {
            val appInfo = packageManager.getApplicationInfo(context.packageName, 0)
            (appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        }.getOrNull().orFalse()
    }
}
