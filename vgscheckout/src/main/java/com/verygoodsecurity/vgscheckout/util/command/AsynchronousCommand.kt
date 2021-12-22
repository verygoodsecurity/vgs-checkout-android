package com.verygoodsecurity.vgscheckout.util.command

internal interface AsynchronousCommand<P, R : Result<*>> {

    fun execute(parameter: P, onResult: (R) -> Unit): VGSCancellable
}