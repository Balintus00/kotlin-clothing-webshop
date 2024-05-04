import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.apollo.kotlin)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "client"
            isStatic = true
        }
    }

    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "kotlinClothingWebshop"
        browser {
            commonWebpackConfig {
                outputFileName = "kotlinClothingWebshop.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.apollo.kotlin.runtime)
                implementation(compose.material3)
                implementation(compose.runtime)
                implementation(compose.ui)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.activity.compose)
                implementation(libs.compose.jetpack.preview)
            }
        }
    }

    jvmToolchain(libs.versions.java.get().toInt())
}

android {
    namespace = "hu.bme.aut.ixnoyb.kotlinclothingwebshop.client"
    compileSdk = libs.versions.android.sdk.compile.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "hu.bme.aut.ixnoyb.kotlinclothingwebshop.client"
        minSdk = libs.versions.android.sdk.min.get().toInt()
        targetSdk = libs.versions.android.sdk.compile.get().toInt()
        versionCode = 1
        versionName = libs.versions.kotlinClothingWebshop.get()

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        val javaVersionEnumName = "VERSION_${libs.versions.java.get()}"

        sourceCompatibility = JavaVersion.valueOf(javaVersionEnumName)
        targetCompatibility = JavaVersion.valueOf(javaVersionEnumName)
    }
    dependencies {
        debugImplementation(libs.compose.jetpack.tooling)
    }
}

compose.desktop {
    application {
        mainClass = "hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.MainKt"
    }
}

compose.experimental {
    web.application {}
}

apollo {
    service("kotlinClothingWebshop") {
        packageName.set("hu.bme.aut.ixnoyb.kotlinclothingwebshop")

        introspection {
            endpointUrl.set("http://localhost:5400/graphql")
            schemaFile.set(file("src/commonMain/graphql/hu/bme/aut/ixnoyb/kotlinclothingwebshop/client/schema.graphqls"))
        }
    }
}