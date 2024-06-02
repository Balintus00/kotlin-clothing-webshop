package hu.bme.aut.ixnoyb.kotlinclothingwebshop.structuredPersistentDatasource

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.db.SqlDriver

fun createDatabase(driverFactory: DriverFactory): SqlDelightDatabase {
    return SqlDelightDatabase(
        articleAdapter = Article.Adapter(
            article_indexAdapter = EnumColumnAdapter(),
            colorAdapter = EnumColumnAdapter(),
            garment_groupAdapter = EnumColumnAdapter(),
            graphical_appearanceAdapter = EnumColumnAdapter(),
            shadeAdapter = EnumColumnAdapter(),
        ),
        driver = driverFactory.createDriver(),
    )
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class DriverFactory {

    fun createDriver(): SqlDriver
}