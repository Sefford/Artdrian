plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.serialization)
    id("com.karumi.kotlin-snapshot")
}

android {
    namespace = "com.sefford.artdrian"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sefford.artdrian"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.sefford.artdrian.test.ArtdrianTestRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-Xjvm-default=all-compatibility",
            "-Xopt-in=kotlin.RequiresOptIn"
        )
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    // KSP dependencies
    ksp(libs.dagger.compiler)

    // Implementation dependencies
    // Android
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.tooling)
    implementation(libs.compose.test.manifest)
    implementation(libs.coil.compose)
    implementation(libs.material3)
    implementation(libs.material.icons.extended)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.permissions)

    // Kotlin
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.serialization.json)
    implementation(libs.kotlin.datetime)

    // Networking
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    // Other
    implementation(libs.arrow.core)
    implementation(libs.dagger)

    // Test dependencies
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.jqwik)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.robolectric)
    testImplementation(libs.kotlinx.coroutines.test)
}
