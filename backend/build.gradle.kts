@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.kotlin.jvm)
    application
    alias(libs.plugins.kotlinx.serialization)
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
    implementation(projects.domain)

    implementation(libs.bouncyCastle)
    implementation(libs.graphql.kotlin.ktor.server)
    implementation(libs.jclOverSlf4j)
    implementation(libs.kermit)
    implementation(libs.kermit.koin)
    implementation(libs.koin.core)
    implementation(libs.koin.ktor)
    implementation(libs.komapper.dialect.postgresql.r2dbc)
    implementation(libs.komapper.starter.r2dbc)
    implementation(libs.kotlin.dl.onnx)
    implementation(libs.ktor.kotlinx.serialization.json.server)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.contentNegotiation)
    implementation(libs.ktor.server.cors)
    implementation(libs.spring.security.crypto)

    runtimeOnly(libs.komapper.slf4j)

    ksp(libs.komapper.processor)
}

tasks.register<Copy>("copyProjectBuildGradle") {
    from("../build.gradle.kts")
    into("rootProject")
}

tasks.register<Copy>("copySettingsGradle") {
    from("../settings.gradle.kts")
    into("rootProject")
}

tasks.register<Copy>("copyVersionCatalog") {
    from("../gradle/libs.versions.toml")
    into("rootProject/gradle")
}

tasks.register<Copy>("copyDomainBuildGradle") {
    from("../domain/build.gradle.kts")
    into("rootProject/domain")
}

tasks.register<Copy>("copyDomainSrc") {
    from("../domain/src")
    into("rootProject/domain/src")
}