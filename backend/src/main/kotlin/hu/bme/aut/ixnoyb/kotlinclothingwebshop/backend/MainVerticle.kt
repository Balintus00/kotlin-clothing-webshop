package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.grpc.ClothingWebshopGrpcService
import io.vertx.core.logging.SLF4JLogDelegateFactory
import io.vertx.grpc.server.GrpcServer
import io.vertx.grpc.server.GrpcServiceBridge
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import io.vertx.pgclient.PgConnectOptions
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.SqlClient
import org.slf4j.LoggerFactory

@Suppress("unused")
class MainVerticle : CoroutineVerticle() {

    private val postgresConnectionOptions = PgConnectOptions()
        .setPort(5432)
        .setHost("localhost")
        .setDatabase("postgres")
        .setUser("postgres")
        .setPassword("password")

    private val postgresPoolOptions = PoolOptions()
        .setMaxSize(5)

    private lateinit var postgresClient: SqlClient

    override suspend fun start() {
        setupVertxLogging()

        postgresClient = PgPool.client(vertx, postgresConnectionOptions, postgresPoolOptions)

        val portNumber = 5400

        val grpcServer = GrpcServer.server(vertx)
        val service = ClothingWebshopGrpcService(postgresClient)
        val serverStub = GrpcServiceBridge.bridge(service)
        serverStub.bind(grpcServer)

        val httpServer = try {
            vertx
                .createHttpServer()
                .requestHandler(grpcServer)
                .listen(portNumber)
                .await()
        } catch (t: Throwable) {
            logger.error("Failed to start HTTP server", t)
            null
        }

        httpServer?.let {
            logger.info("HTTP server started on port ${it.actualPort()}")
        }
    }

    private fun setupVertxLogging() {
        val logFactory = System.getProperty(VERTX_LOGGER_DELEGATE_FACTORY_CLASS_KEY)
        if (logFactory == null) {
            System.setProperty(
                    VERTX_LOGGER_DELEGATE_FACTORY_CLASS_KEY,
                    SLF4JLogDelegateFactory::class.qualifiedName!!
            )
        }
    }

    override suspend fun stop() {
        postgresClient.close().await()
        super.stop()
    }

    companion object {
        private const val VERTX_LOGGER_DELEGATE_FACTORY_CLASS_KEY =
            "org.vertx.logger-delegate-factory-class-name"

        @JvmStatic
        private val logger = LoggerFactory.getLogger(MainVerticle::class.java)
    }
}