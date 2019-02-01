package it.zehus.swipetounlockbutton

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.swipe_to_unlock_button.view.*
import android.util.Log


/**
 * SwipeToUnlock
 * Created by Nicola on 2/1/2019.
 * Copyright © 2019 Zehus. All rights reserved.
 */
class SwipeToUnlockButton @kotlin.jvm.JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var initialY = 0
    private var previousX = 0f
    private var previousY = 0f
    private var yLimit = 10.0f


    init {
        initLayout()
    }

    private fun initLayout() {
        inflate(context, R.layout.swipe_to_unlock_button, this)
        previousX = ibUnlock.x
        previousY = ibUnlock.y

        ibUnlock.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {

                val x: Float = event.x
                val y: Float = event.y

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        return true
                    }
                    // movement
                    MotionEvent.ACTION_MOVE -> {
                        var dy: Float = y - previousY
                        if (dy >= yLimit) {
                            ibUnlock.y = dy

                            previousY = dy
                        }
                        return true
                    }
                    // release
                    MotionEvent.ACTION_UP -> {
                        return true
                    }
                    else -> {
                        return true
                    }
                }
            }
        })
    }

    companion object {
        val TAG = SwipeToUnlockButton::class.simpleName
    }
}