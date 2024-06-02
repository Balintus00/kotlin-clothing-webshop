plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    jvm()

    applyDefaultHierarchyTemplate()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.domain)

                implementation(libs.sqldelight.coroutines)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.sqldelight.driver.android)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.sqldelight.driver.jvm)
            }
        }

        val iosMain by getting {
            dependencies {
                implementation(libs.sqldelight.driver.native)
            }
        }
    }

    jvmToolchain(libs.versions.java.get().toInt())

    task("testClasses") // TODO https://youtrack.jetbrains.com/issue/IDEA-348814/Android-Studio-Iguana-breaks-KMP-compilation
}

android {
    namespace = "hu.bme.aut.ixnoyb.kotlinclothingwebshop.structuredPersistentDatasource"

    compileSdk = libs.versions.android.sdk.compile.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.sdk.min.get().toInt()
    }
}

sqldelight {
    databases {
        create("SqlDelightDatabase") {
            packageName.set("hu.bme.aut.ixnoyb.kotlinclothingwebshop.structuredPersistentDatasource")
        }
    }
}