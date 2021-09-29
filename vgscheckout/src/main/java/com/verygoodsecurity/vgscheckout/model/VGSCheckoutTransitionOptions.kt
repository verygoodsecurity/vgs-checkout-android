@file:Suppress("unused")

package com.verygoodsecurity.vgscheckout.model

import android.content.Context
import android.view.View
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
         * @return [VGSCheckoutTransitionOptions] object that can be used as a parameter of
         * [com.verygoodsecurity.vgscheckout.VGSCheckout.present] function.
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

        /**
         * Create an VGSCheckoutTransitionOptions specifying an animation where the checkout
         * is scaled from a small originating area of the screen to its final full representation.
         *
         * @param source the View that the new activity is animating from. This
         * defines the coordinate space for startX and startY.
         * @param startX the x starting location of the new activity, relative to source.
         * @param startY the y starting location of the activity, relative to source.
         * @param width the initial width of the new activity.
         * @param height the initial height of the new activity.
         * @return [VGSCheckoutTransitionOptions] object that can be used as a parameter of
         * [com.verygoodsecurity.vgscheckout.VGSCheckout.present] function.
         */
        @JvmStatic
        fun makeScaleUpAnimation(
            source: View,
            startX: Int,
            startY: Int,
            width: Int,
            height: Int
        ) = VGSCheckoutTransitionOptions(
            ActivityOptionsCompat.makeScaleUpAnimation(
                source,
                startX,
                startY,
                width,
                height
            )
        )

        /**
         * Create an VGSCheckoutTransitionOptions specifying an animation where the checkout
         * is revealed from a small originating area of the screen to its final full representation.
         *
         * @param source the View that the new activity is animating from. This
         * defines the coordinate space for startX and startY.
         * @param startX the x starting location of the new activity, relative to source.
         * @param startY the y starting location of the activity, relative to source.
         * @param width the initial width of the new activity.
         * @param height the initial height of the new activity.
         * @return [VGSCheckoutTransitionOptions] object that can be used as a parameter of
         * [com.verygoodsecurity.vgscheckout.VGSCheckout.present] function.
         */
        @JvmStatic
        fun makeClipRevealAnimation(
            source: View,
            startX: Int,
            startY: Int,
            width: Int,
            height: Int
        ) = VGSCheckoutTransitionOptions(
            ActivityOptionsCompat.makeClipRevealAnimation(
                source,
                startX,
                startY,
                width,
                height
            )
        )
    }
}