package com.verygoodsecurity.vgscheckout.networking.command.saved

import android.content.Context
import com.verygoodsecurity.vgscheckout.collect.util.extension.concatWithSlash
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.model.Card
import com.verygoodsecurity.vgscheckout.networking.client.HttpMethod
import com.verygoodsecurity.vgscheckout.networking.client.HttpRequest
import com.verygoodsecurity.vgscheckout.networking.client.HttpResponse
import com.verygoodsecurity.vgscheckout.networking.command.Command
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

internal class GetSavedCardsCommand constructor(context: Context) :
    Command<GetSavedCardsCommand.Params, GetSavedCardsCommand.Result>(context) {

    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    private var fetchTask: Future<*>? = null

    override fun run(params: Params, onResult: (Result) -> Unit) {
        fetchTask = executor.submit {
            val cards = mutableListOf<Card>()
            val headers = mapOf(
                AUTHORIZATION_HEADER_KEY to String.format(
                    AUTHORIZATION_HEADER_VALUE,
                    params.accessToken
                )
            )
            params.ids.forEach { id ->
                val response = client.execute(createRequest(id, headers, params))
                parseResponse(response)?.let {
                    cards.add(it)
                }
            }
            onResult.invoke(Result.Success(cards))
        }
    }

    override fun map(exception: VGSCheckoutException) = Result.Failure(exception)

    override fun cancel() {
        fetchTask?.cancel(true)
    }

    private fun createRequest(
        id: String,
        headers: Map<String, String>,
        params: Params
    ) = HttpRequest(
        (params.url concatWithSlash params.path) concatWithSlash id,
        Unit,
        headers = headers,
        method = HttpMethod.GET
    )

    private fun parseResponse(response: HttpResponse): Card? {
        return TODO("Implement")
    }

    companion object {

        private const val AUTHORIZATION_HEADER_KEY = "Authorization"
        private const val AUTHORIZATION_HEADER_VALUE = "Bearer [%s]"
    }

    internal data class Params constructor(
        val url: String,
        val path: String,
        val accessToken: String,
        val ids: List<String>
    ) : Command.Params()

    internal sealed class Result : Command.Result() {

        data class Success(val cards: List<Card>) : Result()

        data class Failure(val exception: VGSCheckoutException) : Result()
    }
}