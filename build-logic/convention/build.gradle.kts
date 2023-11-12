import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.kotlin.compose_mvi.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.firebase.crashlytics.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = "com.kotlin.compose_mvi.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplication") {
            id = "com.kotlin.compose_mvi.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidFirebase") {
            id = "com.kotlin.compose_mvi.android.firebase"
            implementationClass = "AndroidFirebaseConventionPlugin"
        }
        register("androidHilt") {
            id = "com.kotlin.compose_mvi.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "com.kotlin.compose_mvi.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "com.kotlin.compose_mvi.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidNavigation") {
            id = "com.kotlin.compose_mvi.android.navigation"
            implementationClass = "AndroidNavigationConventionPlugin"
        }
        register("jvmLibrary") {
            id = "com.kotlin.compose_mvi.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}
