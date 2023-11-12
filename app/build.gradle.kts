plugins {
    id("com.kotlin.compose_mvi.android.application")
    id("com.kotlin.compose_mvi.android.application.compose")
    id("com.kotlin.compose_mvi.android.hilt")
    id("com.kotlin.compose_mvi.android.navigation")
    id("kotlin-parcelize")
}

android {
    namespace = "com.kotlin.compose_mvi"
    defaultConfig {
        applicationId = "com.kotlin.compose_mvi"
        versionCode = 1
        versionName = "1.0.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }


    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.hilt.navigation.compose)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.material)
    api(libs.androidx.compose.material3)

    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.constraintlayout.compose)

//    implementation(libs.androidx.lifecycle.common.java8)
//    implementation(libs.androidx.lifecycle.precess)
//    implementation(libs.androidx.lifecycle.runtime.compose)
//    implementation(libs.androidx.lifecycle.runtime.ktx)
//    implementation(libs.androidx.lifecycle.service)
//    implementation(libs.androidx.lifecycle.viewmodel.compose)
//    implementation(libs.androidx.lifecycle.viewmodel.ktx)
//    implementation(libs.androidx.lifecycle.viewmodel.savedstate)


//    implementation(libs.kotlinx.coroutines.android)
//    implementation(libs.kotlinx.datetime)
//    implementation(libs.kotlinx.immutable)

//    implementation(libs.core.ktx)
//    implementation(libs.projects.core.navigation)
//    implementation(libs.lifecycle.runtime.ktx)
//    implementation(libs.activity.compose)
//    implementation(platform(libs.compose.bom))
//    implementation(libs.ui)
//    implementation(libs.ui.graphics)
//    implementation(libs.ui.tooling.preview)
//    implementation(libs.material3)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.test.ext.junit)
//    androidTestImplementation(libs.espresso.core)
//    androidTestImplementation(platform(libs.compose.bom))
//    androidTestImplementation(libs.ui.test.junit4)
//    debugImplementation(libs.ui.tooling)
//    debugImplementation(libs.ui.test.manifest)
}