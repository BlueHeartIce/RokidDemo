package com.blue.glassesapp.common.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.view.GestureDetectorCompat
import com.blankj.utilcode.util.SizeUtils
import com.blue.armobile.R
import kotlin.math.abs
import kotlin.math.sqrt

class DirectionDpadView : View, GestureDetector.OnGestureListener {

    companion object {
        const val TAG = "DirectionDpadView"
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val mPaint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }

    private var mBitmapDpadBg = getSquareBitmap(R.mipmap.dpad_background, SizeUtils.dp2px(240f))
    private var mBitmapDpadMask = getSquareBitmap(R.mipmap.dpad_5way_normal, SizeUtils.dp2px(240f))
    private var mBitmapDpadCenter = getSquareBitmap(R.mipmap.dpad_center_normal, SizeUtils.dp2px(80f))

    private val mGestureDetectorCompat by lazy { GestureDetectorCompat(context, this) }

    private var mOkRadius = 0f
    private var mPressKey = -1
    private var isLongPress = false
    private var mOnKeyListener: OnDirectionKeyListener? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBitmapDpadBg = getSquareBitmap(R.mipmap.dpad_background, width)
        mBitmapDpadMask = getSquareBitmap(R.mipmap.dpad_5way_normal, width)
        mOkRadius = 2 * width / 5f / 2f
        mBitmapDpadCenter = getSquareBitmap(R.mipmap.dpad_center_normal, (mOkRadius * 2).toInt())
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawBitmap(mBitmapDpadBg, 0f, 0f, mPaint)
        canvas.drawBitmap(
            mBitmapDpadCenter,
            (width - mBitmapDpadCenter.width) / 2f,
            (height - mBitmapDpadCenter.height) / 2f,
            mPaint
        )
        canvas.drawBitmap(mBitmapDpadMask, 0f, 0f, mPaint)
    }

    private fun handlePress() {
        val bitmapBgId = when (mPressKey) {
            KeyEvent.KEYCODE_DPAD_CENTER -> R.mipmap.dpad_background
            KeyEvent.KEYCODE_DPAD_UP -> R.mipmap.dpad_up_pressed
            KeyEvent.KEYCODE_DPAD_DOWN -> R.mipmap.dpad_down_pressed
            KeyEvent.KEYCODE_DPAD_LEFT -> R.mipmap.dpad_left_pressed
            KeyEvent.KEYCODE_DPAD_RIGHT -> R.mipmap.dpad_right_pressed
            else -> R.mipmap.dpad_background
        }
        val bitmapCenterId = if (mPressKey == KeyEvent.KEYCODE_DPAD_CENTER)
            R.mipmap.dpad_center_pressed else R.mipmap.dpad_center_normal

        mBitmapDpadBg = getSquareBitmap(bitmapBgId, width)
        mBitmapDpadCenter = getSquareBitmap(bitmapCenterId, (mOkRadius * 2).toInt())
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onKeyDownDetected()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                onKeyUpDetected()
            }
        }
        return mGestureDetectorCompat.onTouchEvent(event) || true
    }

    private fun onKeyDownDetected() {
        mPressKey = detectKey()
        if (mPressKey != -1) {
            mOnKeyListener?.onKeyDown(mPressKey)
        }
        handlePress()
    }

    private fun onKeyUpDetected() {
        if (mPressKey != -1) {
            mOnKeyListener?.onKeyUp(mPressKey)
        }
        isLongPress = false
        mPressKey = -1
        handlePress()
    }

    private fun detectKey(): Int {
        val x = lastTouchX - width / 2f
        val y = lastTouchY - height / 2f
        val radius = sqrt(x * x + y * y)
        if (radius > width / 2f) return -1

        return when {
            abs(x) <= mOkRadius && abs(y) <= mOkRadius -> KeyEvent.KEYCODE_DPAD_CENTER
            abs(y) > mOkRadius && y < 0 && abs(y) > abs(x) -> KeyEvent.KEYCODE_DPAD_UP
            abs(y) > mOkRadius && y > 0 && abs(y) > abs(x) -> KeyEvent.KEYCODE_DPAD_DOWN
            x < 0 && abs(x) > mOkRadius && abs(x) > abs(y) -> KeyEvent.KEYCODE_DPAD_LEFT
            x > 0 && abs(x) > mOkRadius && abs(x) > abs(y) -> KeyEvent.KEYCODE_DPAD_RIGHT
            else -> -1
        }
    }

    private var lastTouchX = 0f
    private var lastTouchY = 0f

    override fun onDown(event: MotionEvent): Boolean {
        lastTouchX = event.x
        lastTouchY = event.y
        return true
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onSingleTapUp(event: MotionEvent): Boolean {
        if (mPressKey != -1) mOnKeyListener?.onClick(mPressKey)
        return true
    }

    override fun onLongPress(event: MotionEvent) {
        if (mPressKey != -1) mOnKeyListener?.onLongPress(mPressKey, true)
        isLongPress = true
    }

    override fun onShowPress(event: MotionEvent) {}

    interface OnDirectionKeyListener {
        fun onClick(keyCode: Int)
        fun onLongPress(keyCode: Int, action: Boolean)
        fun onKeyDown(keyCode: Int)
        fun onKeyUp(keyCode: Int)
    }

    fun setOnDirectionKeyListener(listener: OnDirectionKeyListener) {
        mOnKeyListener = listener
    }

    private fun getSquareBitmap(@DrawableRes resId: Int, width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, resId, options)
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(resources, resId, options)
    }
}
