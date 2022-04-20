package com.verygoodsecurity.vgscheckout.networking.command.saved

import android.content.Context
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.networking.command.Command

internal class GetSavedCardsCommand constructor(context: Context) :
    Command<GetSavedCardsCommand.Params, GetSavedCardsCommand.Result>(context) {

    override fun run(params: Params, onResult: (Result) -> Unit) {
        TODO("Implement")
    }

    override fun map(exception: VGSCheckoutException): Result {
        return Result()
    }

    internal data class Params constructor(
        val url: String,
        val path: String,
        val accessToken: String,
        val ids: List<String>
    ) : Command.Params()

    internal class Result : Command.Result()
}