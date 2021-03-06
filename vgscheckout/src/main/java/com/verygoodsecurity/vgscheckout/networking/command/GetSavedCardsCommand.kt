package com.verygoodsecurity.vgscheckout.networking.command

import android.content.Context
import com.verygoodsecurity.vgscheckout.collect.util.extension.concatWithSlash
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.model.Card
import com.verygoodsecurity.vgscheckout.networking.client.HttpMethod
import com.verygoodsecurity.vgscheckout.networking.client.HttpRequest
import com.verygoodsecurity.vgscheckout.networking.client.HttpResponse
import com.verygoodsecurity.vgscheckout.networking.command.core.Command
import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger
import org.json.JSONObject
import java.util.concurrent.*
import kotlin.concurrent.thread

internal class GetSavedCardsCommand constructor(
    context: Context,
    params: Params
) : Command<GetSavedCardsCommand.Params, GetSavedCardsCommand.Result>(context, params) {

    private var rootThread: Thread? = null
    private val cardFetchExecutor: ExecutorService = createExecutor()

    override fun run(params: Params, onResult: (Result) -> Unit) {
        rootThread = thread(start = true) {
            try {
                val tasks = startRequests(params, createHeaders(params))
                onResult.invoke(
                    Result(
                        true,
                        200,
                        null,
                        null,
                        0,
                        tasks.mapNotNull { it.get() }
                    )
                )
            } catch (e: InterruptedException) {
                VGSCheckoutLogger.warn(message = "GetSavedCardsCommand was canceled.")
            } catch (e: ExecutionException) {
                VGSCheckoutLogger.warn(message = "GetSavedCardsCommand card skipped due failed request.")
            }
        }
    }

    override fun map(params: Params, exception: VGSCheckoutException) = Result(
        false,
        exception.code,
        null,
        exception.message,
        0,
        null
    )

    override fun cancel() {
        super.cancel()
        client.cancelAll()
        rootThread?.interrupt()
        cardFetchExecutor.shutdownNow()
    }

    private fun createExecutor() =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2)

    private fun createRequest(
        id: String,
        headers: Map<String, String>,
        params: Params
    ) = HttpRequest(
        (params.url concatWithSlash params.path) concatWithSlash id,
        null,
        headers = headers,
        method = HttpMethod.GET
    )

    private fun createHeaders(params: Params) = mapOf(
        AUTHORIZATION_HEADER_KEY to String.format(
            AUTHORIZATION_HEADER_VALUE,
            params.accessToken
        )
    )

    @Throws(RejectedExecutionException::class)
    private fun startRequests(params: Params, headers: Map<String, String>): List<Future<Card?>> {
        val result = mutableListOf<Future<Card?>>()
        params.ids.forEach { id ->
            result.add(cardFetchExecutor.submit(Callable {
                val response = client.execute(createRequest(id, headers, params))
                parseResponse(response)
            }))
        }
        return result
    }

    private fun parseResponse(response: HttpResponse): Card? {
        if (response.body.isNullOrEmpty()) {
            return null
        }
        return try {
            val data = JSONObject(response.body).getJSONObject(JSON_KEY_DATA)
            val card = data.getJSONObject(JSON_KEY_CARD)
            Card(
                data.getString(JSON_KEY_ID),
                card.getString(JSON_KEY_HOLDER_NAME),
                card.getString(JSON_KEY_NUMBER),
                card.getString(JSON_KEY_LAST4),
                card.getInt(JSON_KEY_EXPIRY_MONTH),
                card.getInt(JSON_KEY_EXPIRY_YEAR),
                card.getString(JSON_KEY_BRAND),
                Card.Raw(response.isSuccessful, response.code, response.body, response.message)
            )
        } catch (e: Exception) {
            null
        }
    }

    companion object {

        private const val AUTHORIZATION_HEADER_KEY = "Authorization"
        private const val AUTHORIZATION_HEADER_VALUE = "Bearer %s"

        private const val JSON_KEY_DATA = "data"
        private const val JSON_KEY_ID = "id"
        private const val JSON_KEY_CARD = "card"
        private const val JSON_KEY_HOLDER_NAME = "name"
        private const val JSON_KEY_NUMBER = "number"
        private const val JSON_KEY_LAST4 = "last4"
        private const val JSON_KEY_EXPIRY_MONTH = "exp_month"
        private const val JSON_KEY_EXPIRY_YEAR = "exp_year"
        private const val JSON_KEY_BRAND = "brand"
    }

    internal data class Params constructor(
        val url: String,
        val path: String,
        val accessToken: String,
        val ids: List<String>
    ) : Command.Params()

    internal data class Result(
        val isSuccessful: Boolean,
        val code: Int,
        val body: String?,
        val message: String?,
        val latency: Long,
        val cards: List<Card>?
    ) : Command.Result()
}