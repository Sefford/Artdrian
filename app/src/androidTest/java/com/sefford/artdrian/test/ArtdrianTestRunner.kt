package com.sefford.artdrian.test

import android.app.Application
import android.content.Context
import com.karumi.shot.ShotTestRunner

class ArtdrianTestRunner: ShotTestRunner() {

    @Throws(IllegalAccessException::class, ClassNotFoundException::class, InstantiationException::class)
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application? {
        return super.newApplication(cl, TestApplication::class.java.name, context)
    }

}
