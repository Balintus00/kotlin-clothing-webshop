package hu.bme.aut.ixnoyb.kotlinclothingwebshop.structuredPersistentDatasource

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DriverFactory {

    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(SqlDelightDatabase.Schema, DATABASE_NAME)
    }
}

private const val DATABASE_NAME = "kotlin_clothing_webshop_sqldelight.db"