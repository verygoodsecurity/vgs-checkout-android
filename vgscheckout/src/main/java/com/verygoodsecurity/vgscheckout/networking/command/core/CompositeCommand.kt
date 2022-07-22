package com.verygoodsecurity.vgscheckout.networking.command.core

internal class CompositeCommand(
    private val commands: Collection<Command<*, *>>
) : VGSCheckoutCancellable() {

    fun execute(onResult: (List<Command.Result>) -> Unit) {
        val results = mutableListOf<Command.Result>()
        commands.forEach { command ->
            command.execute {
                results.add(it)
                if (results.size == commands.size) onResult.invoke(results)
            }
        }
    }

    override fun cancel() {
        commands.forEach { it.cancel() }
    }
}
