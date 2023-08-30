package io.agora.board.fast.sample.cases.hione

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * handle gesture event, and dispatch to listener
 */
class HiOneGestureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    interface GestureListener {
        fun onLeftSwipe() {}
        fun onRightSwipe() {}
        fun onUpSwipe() {}
        fun onDownSwipe() {}
    }

    private var gestureListener: GestureListener? = null

    fun setGestureListener(listener: GestureListener) {
        gestureListener = listener
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }

            MotionEvent.ACTION_UP -> {
                val endX = event.x
                val endY = event.y

                val deltaX = endX - startX
                val deltaY = endY - startY

                if (Math.abs(deltaX) > SWIPE_THRESHOLD) {
                    if (deltaX > 0) {
                        gestureListener?.onRightSwipe()
                    } else {
                        gestureListener?.onLeftSwipe()
                    }
                } else if (Math.abs(deltaY) > SWIPE_THRESHOLD) {
                    if (deltaY > 0) {
                        gestureListener?.onDownSwipe()
                    } else {
                        gestureListener?.onUpSwipe()
                    }
                }
            }
        }

        return true
    }

    private var startX = 0f
    private var startY = 0f

    companion object {
        private const val SWIPE_THRESHOLD = 100
    }

}