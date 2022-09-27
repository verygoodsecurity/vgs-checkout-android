package com.verygoodsecurity.vgscheckout.view.card.cvc

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.view.card.CardType
import com.verygoodsecurity.vgscheckout.collect.view.cvc.CVCIconAdapter
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [32])
class CvcIconAdapterTest {

    private lateinit var adapter: IconAdapterFake
    private lateinit var overrideIconDrawable: Drawable
    private val dummyRect = Rect()

    @Before
    fun setupAdapter() {
        with(Robolectric.buildActivity(Activity::class.java).get()) {
            adapter = IconAdapterFake(this)
            overrideIconDrawable =
                AppCompatResources.getDrawable(this, R.drawable.vgs_checkout_ic_card_back_preview_dark_4)!!
        }
    }

    @Test
    fun getIcon_defaultBounds_defaultBoundsReturned() {
        // Act
        val item = adapter.getItem(CardType.UNKNOWN, "", 3, dummyRect)

        // Assert
        assertEquals(item.bounds.height(), adapter.getDefaultHeight())
        assertEquals(item.bounds.width(), adapter.getDefaultWidth())
    }

    @Test
    fun getIcon_overrideBounds_newBoundsReturned() {
        // Arrange
        val height = 100
        val width = 100
        overrideItemSize(height, width)
        // Act
        val item = adapter.getItem(CardType.UNKNOWN, "", 3, dummyRect)
        // Assert
        assertEquals(item.bounds.height(), height)
        assertEquals(item.bounds.width(), width)
    }

    @Test
    fun getIcon_overrideIcon_overrideIconReturned() {
        // Arrange
        overrideIcon(overrideIconDrawable)
        // Act
        val item = adapter.getItem(CardType.AMERICAN_EXPRESS, "", 4, dummyRect)
        // Assert
        assertEquals(item.constantState, overrideIconDrawable.constantState)
    }

    @Suppress("SameParameterValue")
    private fun overrideItemSize(height: Int, width: Int) {
        adapter.setSize(height, width)
    }

    private fun overrideIcon(drawable: Drawable) {
        adapter.setIcon(drawable)
    }

    internal class IconAdapterFake(context: Context) : CVCIconAdapter(context) {

        private var rect: Rect? = null
        private var drawable: Drawable? = null

        override fun getDefaultBounds(): Rect {
            return rect ?: super.getDefaultBounds()
        }

        override fun getIcon(
            cardType: CardType,
            cardBrand: String?,
            cvcLength: Int,
            r: Rect
        ): Drawable {
            return drawable ?: super.getIcon(cardType, cardBrand, cvcLength, r)
        }

        fun setSize(height: Int, width: Int) {
            rect = Rect(0, 0, width, height)
        }

        fun setIcon(drawable: Drawable) {
            this.drawable = drawable
        }

        fun getDefaultHeight() = getDefaultBounds().height()

        fun getDefaultWidth() = getDefaultBounds().width()
    }
}