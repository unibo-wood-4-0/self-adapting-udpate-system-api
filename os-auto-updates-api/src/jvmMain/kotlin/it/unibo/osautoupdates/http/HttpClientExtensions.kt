package it.unibo.osautoupdates.http

import arrow.core.raise.Raise
import arrow.core.raise.catch
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.HttpResponse
import io.ktor.client.utils.DEFAULT_HTTP_BUFFER_SIZE
import io.ktor.http.HttpStatusCode.Companion.MultipleChoices
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readRemaining
import it.unibo.osautoupdates.failure.DeploymentFailure
import it.unibo.osautoupdates.util.ArrowExtensions.raise
import java.io.File
import kotlinx.io.readByteArray

private val logger = KotlinLogging.logger {}

/**
 * Extensions for the [HttpClient] class.
 */
object HttpClientExtensions {
    /**
     * Downloads a file from the given URL and saves it to the given destination.
     * @param url the URL of the file to download.
     * @param file the file where the content will be saved.
     * @receiver raise a [DeploymentFailure.FetchFailure] if the download fails.
     * @return the corresponding [HttpResponse].
     */
    context(_: Raise<DeploymentFailure.FetchFailure>)
    suspend fun HttpClient.download(
        url: String,
        file: File,
    ): HttpResponse = download(url) { response -> response.byteReadChannelToFile(file) }

    /**
     * Downloads a file from the given URL and uses the corresponding [HttpResponse] to perform an action.
     * @param url the URL of the file to download.
     * @param action the action to perform on the [HttpResponse].
     * @receiver raise a [DeploymentFailure.FetchFailure] if the download fails.
     * @return the corresponding [HttpResponse].
     */
    context(_: Raise<DeploymentFailure.FetchFailure>)
    suspend fun HttpClient.download(
        url: String,
        action: suspend (HttpResponse) -> Unit,
    ): HttpResponse {
        logger.info { "Starting download process..." }
        val clientConfig: HttpRequestBuilder.() -> Unit = {
            onDownload { bytesSentTotal, contentLength ->
                logger.debug { "Downloaded $bytesSentTotal of $contentLength" }
            }
        }
        return catch({
            val response =
                prepareGet(url, clientConfig).execute { response: HttpResponse ->
                    action(response)
                    response
                }
            when (response.status) {
                in OK..<MultipleChoices -> response
                else -> raise(DeploymentFailure.FetchFailure("$url, $response."))
            }
        }) { exception: Exception ->
            raise(DeploymentFailure.FetchFailure("$url, $exception."))
        }
    }

    private suspend fun HttpResponse.byteReadChannelToFile(file: File): HttpResponse {
        val channel: ByteReadChannel = this.body()
        while (!channel.isClosedForRead) {
            val packet = channel.readRemaining(DEFAULT_HTTP_BUFFER_SIZE.toLong())
            while (!packet.exhausted()) {
                file.appendBytes(packet.readByteArray())
            }
        }
        logger.info { "File saved to ${file.name}" }
        return this
    }
}
