package com.verygoodsecurity.vgscheckout.networking.command.saved

import android.content.Context
import com.verygoodsecurity.vgscheckout.collect.util.extension.concatWithSlash
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutCreditCard
import com.verygoodsecurity.vgscheckout.networking.client.HttpMethod
import com.verygoodsecurity.vgscheckout.networking.client.HttpRequest
import com.verygoodsecurity.vgscheckout.networking.client.HttpResponse
import com.verygoodsecurity.vgscheckout.networking.command.Command
import org.json.JSONObject
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

internal class GetSavedCardsCommand constructor(context: Context) :
    Command<GetSavedCardsCommand.Params, GetSavedCardsCommand.Result>(context) {

    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    private var fetchTask: Future<*>? = null

    // TODO: Implement async call for each card request to improve performance
    override fun run(params: Params, onResult: (Result) -> Unit) {
        fetchTask = executor.submit {
            val cards = mutableListOf<VGSCheckoutCreditCard>()
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

    private fun parseResponse(response: HttpResponse): VGSCheckoutCreditCard? {
        if (response.body.isNullOrEmpty()) {
            return null
        }
        return try {
            val data = JSONObject(response.body).getJSONObject(JSON_KEY_DATA)
            val card = data.getJSONObject(JSON_KEY_CARD)
            VGSCheckoutCreditCard(
                data.getString(JSON_KEY_ID),
                card.getString(JSON_KEY_HOLDER_NAME),
                card.getString(JSON_KEY_NUMBER),
                card.getInt(JSON_KEY_EXPIRY_MONTH),
                card.getInt(JSON_KEY_EXPIRY_YEAR),
                card.getString(JSON_KEY_BRAND)
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

    internal sealed class Result : Command.Result() {

        data class Success(val cards: List<VGSCheckoutCreditCard>) : Result()

        data class Failure(val exception: VGSCheckoutException) : Result()
    }
}