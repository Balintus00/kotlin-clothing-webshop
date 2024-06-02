package hu.bme.aut.ixnoyb.kotlinclothingwebshop.structuredPersistentDatasource

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(SqlDelightDatabase.Schema, context, DATABASE_NAME)
    }
}

private const val DATABASE_NAME = "kotlin_clothing_webshop_sqldelight.db"