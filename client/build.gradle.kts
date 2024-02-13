import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
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
    jvm()
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.material3)
                implementation(compose.runtime)
                implementation(compose.ui)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(projects.jvmGrpcClient)
                implementation(libs.androidx.core)
                implementation(libs.compose.jetpack.preview)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(projects.jvmGrpcClient)
            }
        }
    }

    jvmToolchain(libs.versions.java.get().toInt())
}

android {
    namespace = "hu.bme.aut.ixnoyb.kotlinclothingwebshop.client"

    compileSdk = libs.versions.android.sdk.compile.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.sdk.min.get().toInt()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.jetpack.get()
    }

    dependencies {
        debugImplementation(libs.compose.jetpack.tooling)
    }
}

// TODO is it needed?
dependencies {
    implementation(project(mapOf("path" to ":jvm_grpc_client")))
    implementation(project(mapOf("path" to ":jvm_grpc_client")))
}
