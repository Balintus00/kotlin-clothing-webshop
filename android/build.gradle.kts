@file:Suppress("UnstableApiUsage")
@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

dependencies {
    implementation(projects.client)

    implementation(libs.androidx.activity.compose)
}

android {
    namespace = "hu.bme.aut.ixnoyb.kotlinclothingwebshop.android"
    compileSdk = libs.versions.android.sdk.compile.get().toInt()

    defaultConfig {
        applicationId = "hu.bme.aut.ixnoyb.kotlinclothingwebshop.android"
        minSdk = libs.versions.android.sdk.min.get().toInt()
        targetSdk = libs.versions.android.sdk.compile.get().toInt()
        versionCode = 1
        versionName = libs.versions.kotlin.clothing.webshop.get()

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        val javaVersionEnumName = "VERSION_${libs.versions.java.get()}"

        sourceCompatibility = JavaVersion.valueOf(javaVersionEnumName)
        targetCompatibility = JavaVersion.valueOf(javaVersionEnumName)
    }
    kotlinOptions {
        jvmTarget = libs.versions.java.get()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.jetpack.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}