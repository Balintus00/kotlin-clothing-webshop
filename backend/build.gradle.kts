@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.kotlin.jvm)
    application
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktor)
}

application {
    mainClass.set("hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

dependencies {
    implementation(libs.graphql.kotlin.ktor.server)
    implementation(libs.komapper.dialect.postgresql.r2dbc)
    implementation(libs.komapper.r2dbc)
    implementation(libs.kotlin.dl.onnx)
    implementation(libs.ktor.server.cio.jvm)
    implementation(libs.ktor.server.cors)
    implementation(libs.log4j2.api)
    implementation(libs.log4j2.core)
    implementation(libs.log4j2.slfj.impl)
    ksp(libs.komapper.processor)
}