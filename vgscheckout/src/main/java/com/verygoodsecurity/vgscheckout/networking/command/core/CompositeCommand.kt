package com.verygoodsecurity.vgscheckout.networking.command.core

import android.content.Context
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException

internal class CompositeCommand(
    context: Context,
    private val commands: Collection<Command<*, *>>
) : Command<CompositeCommand.Params, CompositeCommand.Result>(context, Params(commands)) {

    private var processedCommandsCounter = 0

    override fun run(params: Params, onResult: (Result) -> Unit) {
        params.commands.forEach { command ->
            command.execute {
                processedCommandsCounter++
                onResult(Result(it, isProcessing()))
            }
        }
    }

    override fun map(params: Params, exception: VGSCheckoutException): Result = Result()

    private fun isProcessing(): Boolean {
        return commands.size != processedCommandsCounter
    }

    override fun cancel() {
        commands.forEach { it.cancel() }
    }

    internal data class Params(
        val commands: Collection<Command<*, *>>
    ) : Command.Params()

    internal data class Result(
        val intermediateResult: Command.Result? = null,
        val isProcessing: Boolean = false
    ) : Command.Result()

}
