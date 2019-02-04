package it.zehus.swipetounlockbutton

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.support.animation.DynamicAnimation
import android.support.animation.SpringAnimation
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.swipe_to_unlock_button.view.*


/**
 * SwipeToUnlock
 * Created by Nicola on 2/1/2019.
 * Copyright © 2019 Zehus. All rights reserved.
 */
class SwipeToUnlockButton @kotlin.jvm.JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var yLimit = 0


    init {
        initLayout()
    }

    private fun initLayout() {
        inflate(context, R.layout.swipe_to_unlock_button, this)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        var dY = 0f

        var yUpperLimit = clContainer.y + 10
        var btUnlockInitialY = ibAction.y
        var unlockCompleted = true

        lateinit var unlockAnimation: AnimationDrawable
        lateinit var lockAnimation: AnimationDrawable

        ibAction.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                Log.v(TAG, "Position x: $v.x, position y:$v.y")
                Log.v(TAG, "Position event x: $event.x, position event y:$event.y")
                Log.v(TAG, "Unlock icon position y:${ivUnlocked.y}")
                Log.v(TAG, "Lock icon position y:${ivLocked.y}")
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dY = v.y - event.rawY
                        if (unlockCompleted) {
                            ibAction.apply {
                                setImageResource(R.drawable.anim_unlock)
                                unlockAnimation = ibAction.drawable as AnimationDrawable
                                unlockAnimation.start()
                            }
                        } else {
                            ibAction.apply {
                                setImageResource(R.drawable.anim_lock)
                                lockAnimation = ibAction.drawable as AnimationDrawable
                                lockAnimation.start()
                            }
                        }
                    }
                    // movement
                    MotionEvent.ACTION_MOVE -> {
                        val newPositionY = event.rawY + dY
                        if (newPositionY in yUpperLimit..btUnlockInitialY) {
                            v.animate()
                                .x(v.x)
                                .y(newPositionY)
                                .setDuration(0)
                                .start()
                        }
                    }
                    // release
                    MotionEvent.ACTION_UP -> {
                        val draggedDistance = ivLocked.y - v.y
                        unlockCompleted = draggedDistance >= (ivLocked.y - ivUnlocked.y) / 2
                        if (unlockCompleted) {
                            ibAction.let {
                                SpringAnimation(
                                    it,
                                    DynamicAnimation.TRANSLATION_Y,
                                    -(ivLocked.y - ivUnlocked.y)
                                ).apply {
                                    start()
                                }
                                ibAction.setImageResource(R.drawable.ic_lock_open)
                            }
                        } else {
                            ibAction.let {
                                SpringAnimation(it, DynamicAnimation.TRANSLATION_Y, 0f).apply {
                                    start()
                                }
                                ibAction.setImageResource(R.drawable.ic_lock_closed)
                            }
                        }
                    }
                }
                return true
            }
        })
    }

    companion object {
        val TAG = SwipeToUnlockButton::class.simpleName
    }
}