package com.verygoodsecurity.vgscheckout.collect.core.model.network

/**
 * The base class definition for a VGSCollect response states.
 */
internal sealed class VGSResponse {

    /**
     * The response code from server.
     */
    abstract val code: Int

    /**
     * The response string.
     */
    abstract val body: String?

    /**
     * The request execution time.
     */
    abstract val latency: Long

    override fun toString(): String = "Code: $code \n $body"

    /**
     * The class definition for a success response state.
     */
    data class SuccessResponse(
        override val code: Int = -1,
        override val body: String? = null,
        override val latency: Long = 0
    ) : VGSResponse()

    /**
     * The class definition for an error response state.
     *
     * @param message Error message.
     */
    data class ErrorResponse(
        val message: String = DEFAULT_ERROR_MESSAGE,
        override val code: Int = -1,
        override val body: String? = null,
        override val latency: Long = 0
    ) : VGSResponse() {

        companion object {

            private const val DEFAULT_ERROR_MESSAGE = "Network request error."
        }
    }
}