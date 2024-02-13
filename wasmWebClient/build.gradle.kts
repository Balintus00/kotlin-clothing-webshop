import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    @OptIn(org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl::class)
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
        val wasmJsMain by getting {
            dependencies {
                implementation(projects.client)
                implementation(compose.ui)
            }
        }
    }
}

compose.experimental {
    web.application {}
}