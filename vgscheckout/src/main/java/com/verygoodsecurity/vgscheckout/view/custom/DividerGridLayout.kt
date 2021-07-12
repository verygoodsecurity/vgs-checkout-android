package com.verygoodsecurity.vgscheckout.view.custom

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.GridLayout
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.core.graphics.and
import androidx.core.view.updatePadding
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.util.extension.*
import kotlin.math.max

internal open class DividerGridLayout @JvmOverloads constructor(
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

    private var selectedId: Int? = null

    init {
        isEnabled = false
        context.getStyledAttributes(attrs, R.styleable.DividerGridLayout) {
            cornersRadius = getDimension(
                R.styleable.DividerGridLayout_dgl_corners_radius,
                DEFAULT_CORNERS_RADIUS
            )
            gridWidth = getDimensionPixelOffset(
                R.styleable.DividerGridLayout_dgl_grid_width,
                DEFAULT_GRID_WIDTH
            )
            gridColor = getColor(
                R.styleable.DividerGridLayout_dgl_grid_color,
                DEFAULT_GRID_COLOR
            )
            gridColorStateList = getColorStateListOrNull(
                R.styleable.DividerGridLayout_dgl_grid_color
            )
            selectedGridColor = getColor(
                R.styleable.DividerGridLayout_dgl_selected_grid_color,
                DEFAULT_GRID_COLOR
            )
            if (gridWidth.isPositive()) {
                gridPaint = Paint().apply {
                    color = gridColorStateList.getColor(drawableState, gridColor)
                    style = Paint.Style.STROKE
                    strokeWidth = gridWidth.toFloat()
                }
                selectedGridPaint = Paint().apply {
                    color = selectedGridColor
                    style = Paint.Style.STROKE
                    strokeWidth = gridWidth.toFloat()
                }
            }
            updatePadding()
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        return DividerGridLayoutState(super.onSaveInstanceState()).apply {
            this.gridColor = this@DividerGridLayout.gridColor
            this.selectedGridColor = this@DividerGridLayout.selectedGridColor
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        (state as? DividerGridLayoutState)?.let {
            setGridColor(it.gridColor)
            setSelectedGridColor(it.selectedGridColor)
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
        canvas?.let {
            canvas.save()
            applyRoundedCorners(canvas)
            super.draw(canvas)
            canvas.restore()
        } ?: super.draw(canvas)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        canvas?.let {
            canvas.save()
            applyRoundedCorners(canvas)
            super.dispatchDraw(canvas)
            canvas.restore()
            drawDividers(canvas)
        } ?: super.dispatchDraw(canvas)
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
    }

    internal class DividerGridLayoutState : BaseSavedState {

        var gridColor: Int = DEFAULT_GRID_COLOR
        var selectedGridColor: Int = DEFAULT_GRID_COLOR

        constructor(superState: Parcelable?) : super(superState)

        constructor(`in`: Parcel) : super(`in`) {
            gridColor = `in`.readInt()
            selectedGridColor = `in`.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(gridColor)
            out.writeInt(selectedGridColor)
        }

        companion object {

            @Suppress("unused")
            @JvmField
            val CREATOR = object : Parcelable.Creator<DividerGridLayoutState> {
                override fun createFromParcel(source: Parcel): DividerGridLayoutState {
                    return DividerGridLayoutState(source)
                }

                override fun newArray(size: Int): Array<DividerGridLayoutState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}