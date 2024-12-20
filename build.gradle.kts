plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(libs.snapshot)
        classpath(libs.screenshot)
    }
}

allprojects {
    tasks.withType<Test> {
        jvmArgs(
            "--add-opens", "java.base/java.time=ALL-UNNAMED",
            "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED",
            "--add-opens", "java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED",
            "--add-opens", "java.base/java.util=ALL-UNNAMED",
            "--add-opens", "java.base/java.lang=ALL-UNNAMED"
        )
    }
}
