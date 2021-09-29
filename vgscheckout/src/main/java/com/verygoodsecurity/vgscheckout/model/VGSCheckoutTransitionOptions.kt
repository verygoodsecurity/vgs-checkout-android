package com.verygoodsecurity.vgscheckout.model

import android.content.Context
import androidx.core.app.ActivityOptionsCompat

/**
 * Helper class for specifying custom animation to run when the checkout is displayed.
 */
class VGSCheckoutTransitionOptions private constructor(internal val options: ActivityOptionsCompat) {

    companion object {

        /**
         * Create an VGSCheckoutTransitionOptions specifying a custom animation to run when the
         * checkout is displayed.
         *
         * @param context who is defining this. This is the application that the
         * animation resources will be loaded from.
         * @param enterAnimId a resource ID of the animation resource to use for the
         * incoming activity. Use 0 for no animation.
         * @param exitAnimId a resource ID of the animation resource to use for the
         * outgoing activity. Use 0 for no animation.
         */
        @JvmStatic
        fun makeCustomAnimation(context: Context, enterAnimId: Int, exitAnimId: Int) =
            VGSCheckoutTransitionOptions(
                ActivityOptionsCompat.makeCustomAnimation(
                    context,
                    enterAnimId,
                    exitAnimId
                )
            )
    }
}