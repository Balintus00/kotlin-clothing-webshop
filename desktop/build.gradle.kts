@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.multiplatform)
    application
}

dependencies {
    implementation(projects.client)
    implementation(compose.desktop.currentOs)
}

application {
    mainClass.set("hu.bme.aut.ixnoyb.kotlinclothingwebshop.desktop.MainKt")
}