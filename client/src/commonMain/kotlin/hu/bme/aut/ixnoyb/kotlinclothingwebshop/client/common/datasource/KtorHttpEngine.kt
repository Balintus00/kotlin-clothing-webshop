package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.datasource

import com.apollographql.apollo3.api.http.HttpHeader
import com.apollographql.apollo3.api.http.HttpMethod
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.apollographql.apollo3.network.http.HttpEngine
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.util.flattenEntries
import okio.Buffer
import kotlin.coroutines.cancellation.CancellationException

// Source: https://github.com/apollographql/apollo-kotlin/blob/main/libraries/apollo-engine-ktor/src/commonMain/kotlin/com/apollographql/apollo3/network/http/KtorHttpEngine.kt
// Note that constructor differs from the source to allow using already instantiated HttpClient.
internal class KtorHttpEngine(private val client: HttpClient): HttpEngine {

    private var disposed = false

    override suspend fun execute(request: HttpRequest): HttpResponse {
        try {
            val response = client.request(request.url) {
                method = when (request.method) {
                    HttpMethod.Get -> io.ktor.http.HttpMethod.Get
                    HttpMethod.Post -> io.ktor.http.HttpMethod.Post
                }
                request.headers.forEach {
                    header(it.name, it.value)
                }
                request.body?.let {
                    header(HttpHeaders.ContentType, it.contentType)
                    val buffer = Buffer()
                    it.writeTo(buffer)
                    setBody(buffer.readUtf8())
                }
            }
            val responseByteArray: ByteArray = response.body()
            val responseBufferedSource = Buffer().write(responseByteArray)
            return HttpResponse.Builder(statusCode = response.status.value)
                .body(responseBufferedSource)
                .addHeaders(response.headers.flattenEntries().map { HttpHeader(it.first, it.second) })
                .build()
        } catch (e: CancellationException) {
            // Cancellation Exception is passthrough
            throw e
        } catch (t: Throwable) {
            throw ApolloNetworkException(t.message, t)
        }
    }

    override fun dispose() {
        if (!disposed) {
            client.close()
            disposed = true
        }
    }
}