package com.verygoodsecurity.vgscheckout.view.custom

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.GridLayout
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.core.graphics.and
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.util.extension.*
import kotlinx.parcelize.Parcelize
import kotlin.math.max

internal class VGSCheckoutDividerGridLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : GridLayout(context, attrs, defStyleAttr) {

    private var cornersRadius: Float = DEFAULT_CORNERS_RADIUS
    private lateinit var roundedCornersPath: Path

    private var gridWidth: Int = DEFAULT_GRID_WIDTH
    private var gridColor: Int = DEFAULT_GRID_COLOR
    private var gridColorStateList: ColorStateList? = null
    private var gridPaint: Paint? = null

    private var selectedGridColor: Int = DEFAULT_GRID_COLOR
    private var selectedGridPaint: Paint? = null

    private var selectedId: Int = DEFAULT_SELECTED_ID

    init {
        isEnabled = true
        context.getStyledAttributes(attrs, R.styleable.VGSCheckoutDividerGridLayout) {
            cornersRadius = getDimension(
                R.styleable.VGSCheckoutDividerGridLayout_vgs_checkout_dgl_corners_radius,
                DEFAULT_CORNERS_RADIUS
            )
            gridWidth = getDimensionPixelOffset(
                R.styleable.VGSCheckoutDividerGridLayout_vgs_checkout_dgl_grid_width,
                DEFAULT_GRID_WIDTH
            )
            gridColor = getColor(
                R.styleable.VGSCheckoutDividerGridLayout_vgs_checkout_dgl_grid_color,
                DEFAULT_GRID_COLOR
            )
            gridColorStateList = getColorStateListOrNull(
                R.styleable.VGSCheckoutDividerGridLayout_vgs_checkout_dgl_grid_color
            )
            selectedGridColor = getColor(
                R.styleable.VGSCheckoutDividerGridLayout_vgs_checkout_dgl_selected_grid_color,
                DEFAULT_GRID_COLOR
            )
            if (gridWidth.isPositive()) {
                gridPaint = Paint().apply {
                    color = gridColorStateList.getColor(drawableState, gridColor)
                    style = Paint.Style.STROKE
                    strokeWidth = gridWidth.toFloat()
                    isAntiAlias = true
                }
                selectedGridPaint = Paint().apply {
                    color = selectedGridColor
                    style = Paint.Style.STROKE
                    strokeWidth = gridWidth.toFloat()
                    isAntiAlias = true
                }
            }
            updatePadding()
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        return VGSCheckoutDividerGridLayoutState(
            super.onSaveInstanceState(),
            gridColor,
            selectedGridColor,
            selectedId
        )
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        (state as VGSCheckoutDividerGridLayoutState).run {
            super.onRestoreInstanceState(superState)
            this@VGSCheckoutDividerGridLayout.selectedId = selectedId
            setGridColor(gridColor)
            setSelectedGridColor(selectedGridColor)
        }
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        val min = gridWidth.half()
        super.setPadding(max(left, min), max(top, min), max(right, min), max(bottom, min))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val contentRectBounds = RectF(
            0f + paddingStart,
            0f + paddingTop,
            width.toFloat() - paddingEnd,
            height.toFloat() - paddingBottom
        )
        this.roundedCornersPath = Path().apply {
            addRoundRect(contentRectBounds, cornersRadius, cornersRadius, Path.Direction.CW)
        }
    }

    override fun draw(canvas: Canvas?) {
        canvas?.let { draw(it) { super.draw(canvas) } } ?: super.draw(canvas)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        if (canvas != null && background == null) {
            draw(canvas) { super.dispatchDraw(canvas) }
        } else {
            super.dispatchDraw(canvas)
        }
    }

    fun setGridColor(@ColorInt color: Int) {
        gridColor = color
        if (gridWidth.isPositive()) {
            gridPaint = Paint().apply {
                this.color = gridColor
                this.style = Paint.Style.STROKE
                this.strokeWidth = gridWidth.toFloat()
            }
            invalidate()
        }
    }

    fun setGridColor(stateList: ColorStateList) {
        gridColorStateList = stateList
        if (gridWidth.isPositive()) {
            gridPaint = Paint().apply {
                this.color = gridColorStateList.getColor(drawableState, gridColor)
                this.style = Paint.Style.STROKE
                this.strokeWidth = gridWidth.toFloat()
            }
            invalidate()
        }
    }

    fun setSelectedGridColor(@ColorInt color: Int) {
        selectedGridColor = color
        if (gridWidth.isPositive()) {
            selectedGridPaint = Paint().apply {
                this.color = color
                this.style = Paint.Style.STROKE
                this.strokeWidth = gridWidth.toFloat()
            }
            invalidate()
        }
    }

    fun setSelected(@IdRes id: Int) {
        this.selectedId = id
        invalidate()
    }

    private fun applyRoundedCorners(canvas: Canvas) {
        canvas.clipPath(roundedCornersPath)
    }

    private fun draw(canvas: Canvas, callSuper: (Canvas) -> Unit) {
        canvas.save()
        applyRoundedCorners(canvas)
        callSuper.invoke(canvas)
        canvas.restore()
        drawDividers(canvas)
    }

    private fun drawDividers(canvas: Canvas) {
        var selected: View? = null // Draw last, to overlap other borders
        for (i in 0 until childCount) {
            val view: View = getChildAt(i)
            if (isSelected(view)) {
                selected = view
                continue
            }
            checkPaintAndDrawBorder(canvas, view, gridPaint)
        }
        selected?.let { checkPaintAndDrawBorder(canvas, it, selectedGridPaint) }
    }

    private fun checkPaintAndDrawBorder(canvas: Canvas, view: View, paint: Paint?) {
        paint?.let { drawBorder(canvas, view, paint) }
    }

    private fun drawBorder(canvas: Canvas, view: View, paint: Paint) {
        if (!view.isVisible) {
            return
        }
        val path = Path().also {
            it.addRect(
                RectF(
                    view.left.toFloat(),
                    view.top.toFloat(),
                    view.right.toFloat(),
                    view.bottom.toFloat()
                ), Path.Direction.CW
            )
        }
        val newPath = path.and(roundedCornersPath)
        canvas.drawPath(newPath, paint)
    }

    private fun isSelected(view: View) = view.id == selectedId

    companion object {

        private const val DEFAULT_CORNERS_RADIUS = 0f
        private const val DEFAULT_GRID_WIDTH = -1
        private const val DEFAULT_GRID_COLOR = Color.BLACK
        private const val DEFAULT_SELECTED_ID = -1
    }

    @Parcelize
    internal class VGSCheckoutDividerGridLayoutState constructor(
        val superState: Parcelable?,
        val gridColor: Int = DEFAULT_GRID_COLOR,
        val selectedGridColor: Int = DEFAULT_GRID_COLOR,
        val selectedId: Int = DEFAULT_SELECTED_ID
    ) : Parcelable
}