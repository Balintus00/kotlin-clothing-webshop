package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.di

import com.apollographql.apollo3.ApolloClient
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.di.articleBrowsingModule
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.datasource.KtorHttpEngine
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.datasource.getServerBaseUrl
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.di.checkoutModule
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.di.userManagementModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

internal val appModule = module {
    includes(articleBrowsingModule, checkoutModule, userManagementModule)

    single {
        HttpClient(getKtorEngine()) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                    }
                )
            }

            install(Logging) {
                logger = object : Logger {

                    val KTOR_LOG_TAG = "HTTP Client"

                    override fun log(message: String) {
                        co.touchlab.kermit.Logger.v(
                            tag = KTOR_LOG_TAG,
                            messageString = message,
                        )
                    }
                }

                level = LogLevel.ALL
            }
        }
    }

    single {
        ApolloClient.Builder()
            .httpEngine(KtorHttpEngine(client = get()))
            .serverUrl("${getServerBaseUrl()}/graphql")
            .build()
    }
}

internal expect fun getKtorEngine(): HttpClientEngineFactory<*>