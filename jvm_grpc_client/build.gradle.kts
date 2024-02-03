@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlin.jvm)
    id("java-library")
    alias(libs.plugins.wire)
}

dependencies {
    implementation(libs.wire.grpc.client)
}

java {
    val javaVersionEnumName = "VERSION_${libs.versions.java.get()}"

    sourceCompatibility = JavaVersion.valueOf(javaVersionEnumName)
    targetCompatibility = JavaVersion.valueOf(javaVersionEnumName)
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

wire {
    kotlin {
        rpcRole = "client"
    }

    sourcePath {
        srcDir("../protos/")
    }
}