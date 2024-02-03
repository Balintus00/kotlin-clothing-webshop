import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.google.protobuf.gradle.id

@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.kotlin.jvm)
    application
    alias(libs.plugins.johnrengelman.shadow)
    alias(libs.plugins.protobuf)
}

val launcherClassName = "io.vertx.core.Launcher"
val mainVerticleName = "hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.MainVerticle"

val doOnChange = "$projectDir/gradlew classes"

application {
    mainClass.set(launcherClassName)
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

dependencies {
    implementation(libs.grpc.kotlin.stub)
    implementation(libs.grpc.protobuf)
    implementation(libs.kotlin.dl.onnx)
    implementation(libs.log4j2.api)
    implementation(libs.log4j2.core)
    implementation(libs.log4j2.slfj.impl)
    implementation(libs.protobuf.kotlin)
    implementation(libs.scram.client)
    implementation(libs.vertx.coroutines)
    implementation(libs.vertx.grpc.server)
    implementation(libs.vertx.kotlin)
    implementation(libs.vertx.postgres.client)

    if (JavaVersion.current().isJava9Compatible) {
        // Workaround for @javax.annotation.Generated
        // see: https://github.com/grpc/grpc-java/issues/3633
        implementation(libs.javax.annotation)
    }

    protobuf(files("../protos/"))
}

protobuf {
    val grpcJavaProtocPluginId = "grpcJavaProtocPluginId"
    val grpcKotlinProtocPluginId = "grpcKotlinProtocPluginId"

    protoc {
        artifact = libs.protoc.get().toString()
    }
    plugins {
        id(grpcJavaProtocPluginId) {
            artifact = libs.plugins.protoc.grpc.java.get().toString()
        }
        id(grpcKotlinProtocPluginId) {
            artifact = "${libs.plugins.protoc.grpc.kotlin.get()}:jdk8@jar"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                // Note that the braces cannot be omitted according to the official sample.
                // https://github.com/google/protobuf-gradle-plugin/blob/master/examples/exampleKotlinDslProject/build.gradle.kts
                id(grpcJavaProtocPluginId) {}
                id(grpcKotlinProtocPluginId) {}
                it.builtins {
                    id("kotlin")
                }
            }
        }
    }
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("fat")
    manifest {
        attributes(mapOf("Main-Verticle" to mainVerticleName))
    }
    mergeServiceFiles()
}

tasks.withType<JavaExec> {
    args = listOf("run", mainVerticleName, "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}