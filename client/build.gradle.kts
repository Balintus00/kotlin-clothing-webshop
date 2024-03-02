import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
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
    jvm()
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
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
                implementation(libs.androidx.core)
                implementation(libs.compose.jetpack.preview)
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

apollo {
    service("kotlinClothingWebshop") {
        packageName.set("hu.bme.aut.ixnoyb.kotlinclothingwebshop")

        introspection {
            endpointUrl.set("http://localhost:5400/graphql")
            schemaFile.set(file("src/commonMain/graphql/hu/bme/aut/ixnoyb/kotlinclothingwebshop/client/schema.graphqls"))
        }
    }
}