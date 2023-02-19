package com.github.aptemkov.loadapp

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var progress = 0.0

    private var valueAnimator = AnimatorInflater.loadAnimator(
        context,
        R.animator.loading_animator
    ) as ValueAnimator

    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new -> }

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 36F
        typeface = Typeface.create("", Typeface.BOLD)
        color = resources.getColor(R.color.colorPrimary)
    }

    private val textRect = Rect()


    init {
        isClickable = true

        valueAnimator.addUpdateListener {
            progress = (it.animatedValue as Float).toDouble()
            invalidate()
            if (progress == 100.0) {
                valueAnimator.cancel()
                makeStateCompleted()
                invalidate()
            }
        }


    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        val text = when (buttonState) {
            ButtonState.Loading -> "Loading"
            else -> "Download"
        }

        drawText(text, canvas)

        if (buttonState == ButtonState.Loading) {

            paint.color = resources.getColor(R.color.colorPrimaryDark)
            canvas?.drawRect(0f,
                0f,
                (widthSize * progress / 100).toFloat(),
                height.toFloat(),
                paint
            )

            paint.getTextBounds(text, 0, text.length, textRect)

            //Circle
            paint.color = resources.getColor(R.color.colorAccent)
            val mid = width / 2.0f
            val diameter = height.toFloat() / 2
            val circlePosition = mid + textRect.right
            val rectangle = RectF(
                /*left =*/ circlePosition,
                /*top =*/ diameter / 2,
                /*right =*/ circlePosition + diameter,
                /*bottom =*/ 1.5f * diameter)
            //canvas?.drawRect(rectangle, paint)
            val arcProportion = (progress / 100).toFloat()
            canvas?.drawArc(rectangle, -90f, arcProportion * 360, true, paint)

        } else if (buttonState == ButtonState.Completed) {
            paint.color = resources.getColor(R.color.colorPrimary)
            canvas?.drawRect(
                0f, 0f,
                (widthSize * (progress / 100)).toFloat(), heightSize.toFloat(), paint
            )
        }

        drawText(text, canvas)
        paint.color = resources.getColor(R.color.colorPrimary)

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    override fun performClick(): Boolean {
        super.performClick()
        if (buttonState == ButtonState.Completed) makeStateLoading()
        valueAnimator.start()
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        widthSize = w
        heightSize = h
    }

    private fun makeStateCompleted() {
        buttonState = ButtonState.Completed
        isClickable = true
    }

    private fun makeStateLoading() {
        buttonState = ButtonState.Loading
        isClickable = false
    }

    private fun drawText(text: String, canvas: Canvas?) {
        paint.color = Color.WHITE
        canvas?.drawText(text, (width / 2).toFloat(), ((height + 30) / 2).toFloat(), paint)
    }

}