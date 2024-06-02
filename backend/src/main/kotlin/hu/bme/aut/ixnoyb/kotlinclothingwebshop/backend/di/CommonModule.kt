package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.di

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.CONFIG_PROPERTY_JWT_SECRET
import io.ktor.server.application.ApplicationEnvironment
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.ConnectionFactoryOptions.DRIVER
import io.r2dbc.spi.ConnectionFactoryOptions.HOST
import io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD
import io.r2dbc.spi.ConnectionFactoryOptions.PORT
import io.r2dbc.spi.ConnectionFactoryOptions.USER
import org.koin.dsl.module
import org.komapper.dialect.postgresql.r2dbc.PostgreSqlR2dbcDialect
import org.komapper.r2dbc.R2dbcDatabase

private const val CONFIG_PROPERTY_DATABASE_HOST_NAME = "ktor.postgresql.host"
private const val CONFIG_PROPERTY_DATABASE_PORT = "ktor.postgresql.port"
private const val CONFIG_PROPERTY_DATABASE_USER = "ktor.postgresql.user"
private const val CONFIG_PROPERTY_DATABASE_PASSWORD = "ktor.postgresql.password"

fun getCommonModule(environment: ApplicationEnvironment) = module {

    single<ApplicationEnvironment> { environment }

    single<ConnectionFactory> {
        ConnectionFactories.get(
            ConnectionFactoryOptions.builder()
                .option(DRIVER, "postgresql")
                .option(
                    HOST,
                    environment.config.property(CONFIG_PROPERTY_DATABASE_HOST_NAME).getString()
                )
                .option(
                    PORT,
                    environment.config.property(CONFIG_PROPERTY_DATABASE_PORT).getString().toInt()
                )
                .option(
                    USER,
                    environment.config.property(CONFIG_PROPERTY_DATABASE_USER).getString()
                )
                .option(
                    PASSWORD,
                    environment.config.property(CONFIG_PROPERTY_DATABASE_PASSWORD).getString()
                )
                .build()
        )
    }

    single<R2dbcDatabase> {
        R2dbcDatabase(
            connectionFactory = get<ConnectionFactory>(),
            dialect = PostgreSqlR2dbcDialect()
        )
    }

    single<JWTVerifier> {
        val configurationEnvironment: ApplicationEnvironment = get()

        JWT.require(
            Algorithm.HMAC256(
                configurationEnvironment.config.property(
                    CONFIG_PROPERTY_JWT_SECRET
                ).getString()
            )
        )
            .build()
    }
}