pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "KotlinClothingWebshop"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":android")
include(":backend")
include(":desktop")
include(":client")
include(":jvm_grpc_client")
include(":backend")
