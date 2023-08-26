// https://youtrack.jetbrains.com/issue/KTIJ-19369
// https://github.com/gradle/gradle/issues/20131
@file:Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}