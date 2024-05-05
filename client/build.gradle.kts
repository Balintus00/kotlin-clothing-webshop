import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.apollo.kotlin)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
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

            export(libs.decompose)
            export(libs.essenty.lifecycle)
            export(libs.mvikotlin.logging)
            export(libs.mvikotlin.main)
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

    applyDefaultHierarchyTemplate()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.decompose)
                api(libs.essenty.lifecycle)
                api(libs.mvikotlin.logging)
                api(libs.mvikotlin.main)

                implementation(projects.domain)

                implementation(compose.material3)
                implementation(compose.materialIconsExtended)   // TODO remove
                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(libs.apollo.kotlin.runtime)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.coroutines.core)
                implementation(libs.decompose.composeExtension)
                implementation(libs.kermit)
                implementation(libs.koin.core)
                implementation(libs.ktor.client.contentNegotiation)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.kotlinx.serialization.json)
                implementation(libs.material3.windowSizeClass)
                implementation(libs.mvikotlin.core)
                implementation(libs.mvikotlin.coroutines)
            }
        }

        val nonWasmJsMain by creating {
            dependsOn(commonMain)

            dependencies {
                implementation(libs.kermit.koin)
            }
        }

        val androidMain by getting {
            dependsOn(nonWasmJsMain)

            dependencies {
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.lifecycle.compose)
                implementation(libs.androidx.splashScreen)
                implementation(libs.compose.jetpack.preview)
                implementation(libs.coroutines.android)
                implementation(libs.koin.android)
                implementation(libs.ktor.client.okhttp)
            }
        }

        val desktopMain by getting {
            dependsOn(nonWasmJsMain)

            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.coroutines.swing)
            }
        }

        val iosMain by getting {
            dependsOn(nonWasmJsMain)

            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }

    jvmToolchain(libs.versions.java.get().toInt())

    task("testClasses") // TODO https://youtrack.jetbrains.com/issue/IDEA-348814/Android-Studio-Iguana-breaks-KMP-compilation
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