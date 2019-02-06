package it.zehus.fancyswitch

import android.content.Context
import android.content.res.TypedArray
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.animation.DynamicAnimation
import android.support.animation.SpringAnimation
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import it.zehus.fancyswitch.FancySwitch.Orientation.*
import kotlinx.android.synthetic.main.fancy_switch_portrait.view.*


/**
 * SwipeToUnlock
 * Created by Nicola on 2/1/2019.
 * Copyright © 2019 Zehus. All rights reserved.
 */
class FancySwitch @kotlin.jvm.JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var backgroundShape = GradientDrawable()
    private var actionOnDrawable: Drawable
    private var actionOnButtonDrawable: Drawable
    private var actionOffDrawable: Drawable
    private var actionOffButtonDrawable: Drawable

    private var actionButtonMargin = 0
    private var mBaseColor: Int
    private var orientation: Int

    private var attributes: TypedArray = context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.FancySwitch,
        0, 0
    )


    init {
        mBaseColor = attributes.getColor(
            R.styleable.FancySwitch_baseColor,
            ContextCompat.getColor(context, R.color.baseBackgroundColor)
        )
        orientation = attributes.getInt(R.styleable.FancySwitch_orientation, PORTRAIT.ordinal)
        actionOnDrawable = attributes.getDrawable(R.styleable.FancySwitch_actionOnDrawable)
        actionOffDrawable = attributes.getDrawable(R.styleable.FancySwitch_actionOffDrawable)
        actionOnButtonDrawable = actionOnDrawable.constantState.newDrawable().mutate()
        actionOffButtonDrawable = actionOffDrawable.constantState.newDrawable().mutate()

        backgroundShape.shape = GradientDrawable.RECTANGLE
        backgroundShape.cornerRadius = CORNER_RADIUS
        backgroundShape.setColor(mBaseColor)
        initLayout()
    }

    private fun initLayout() {
        when (orientation) {
            PORTRAIT.ordinal -> inflate(context, R.layout.fancy_switch_portrait, this)
            LANDSCAPE.ordinal -> inflate(context, R.layout.fancy_switch_land, this)
        }

        clContainer.background = backgroundShape
        clContainer.background.alpha = 102
        actionButtonMargin =
            ((resources.getDimension(R.dimen.iv_action_completed_margin) / resources.displayMetrics.density).toInt())
        ivActionOff.setImageDrawable(actionOffDrawable)
        ivActionOn.setImageDrawable(actionOnDrawable)

        DrawableCompat.setTint(actionOnButtonDrawable, mBaseColor)
        ibAction.setImageDrawable(actionOnButtonDrawable)
        DrawableCompat.setTint(actionOffButtonDrawable, mBaseColor)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        var translationDelta = 0f

        var translationLimit: Float = when (orientation) {
            PORTRAIT.ordinal -> clContainer.y + clContainer.paddingTop
            LANDSCAPE.ordinal -> clContainer.x + clContainer.width + clContainer.paddingStart
            else -> 0f
        }

        var btActionInitialPosition = when (orientation) {
            PORTRAIT.ordinal -> ibAction.y
            LANDSCAPE.ordinal -> ibAction.x
            else -> 0f
        }

        ibAction.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                Log.v(TAG, "Position x: $v.x, position y:$v.y")
                Log.v(TAG, "Position event x: $event.x, position event y:$event.y")
                Log.v(TAG, "Unlock icon position y:${ivActionOn.y}")
                Log.v(TAG, "Lock icon position y:${ivActionOff.y}")
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        when (orientation) {
                            PORTRAIT.ordinal -> translationDelta = v.y - event.rawY
                            LANDSCAPE.ordinal -> translationDelta = v.x - event.rawX
                        }

                    }
                    // movement
                    MotionEvent.ACTION_MOVE -> {
                        val newPosition: PointF
                        when (orientation) {
                            PORTRAIT.ordinal -> {
                                newPosition = PointF(v.x, event.rawY + translationDelta)
                                if (newPosition.y in translationLimit..btActionInitialPosition) {
                                    v.animate()
                                        .y(newPosition.y)
                                        .setDuration(0)
                                        .start()
                                }
                            }
                            LANDSCAPE.ordinal -> {
                                newPosition = PointF(event.rawX + translationDelta, v.y)
                                if (newPosition.x in btActionInitialPosition..translationLimit) {
                                    v.animate()
                                        .x(newPosition.x)
                                        .setDuration(0)
                                        .start()
                                }
                            }
                        }


                    }
                    // release
                    MotionEvent.ACTION_UP -> {
                        val draggedDistance: Float = when (orientation) {
                            PORTRAIT.ordinal -> ivActionOff.y - v.y
                            LANDSCAPE.ordinal -> v.x - ivActionOff.x
                            else -> 0.0f
                        }
                        val actionCompleted: Boolean = when (orientation) {
                            PORTRAIT.ordinal -> draggedDistance >= (ivActionOff.y - ivActionOn.y) / 2
                            LANDSCAPE.ordinal -> draggedDistance >= (ivActionOn.x - ivActionOff.x) / 2
                            else -> false
                        }
                        if (actionCompleted) {
                            when (orientation) {
                                PORTRAIT.ordinal -> {
                                    ibAction.let {
                                        SpringAnimation(
                                            it,
                                            DynamicAnimation.TRANSLATION_Y,
                                            -(ivActionOff.y - ivActionOn.y - actionButtonMargin)
                                        ).apply {
                                            start()
                                        }
                                        it.setImageDrawable(actionOffButtonDrawable)
                                    }
                                }
                                LANDSCAPE.ordinal -> {
                                    ibAction.let {
                                        SpringAnimation(
                                            it,
                                            DynamicAnimation.TRANSLATION_X,
                                            (ivActionOn.x - ivActionOff.x - actionButtonMargin)
                                        ).apply {
                                            start()
                                        }
                                        it.setImageDrawable(actionOffButtonDrawable)
                                    }
                                }
                            }
                            clContainer.background.alpha = 255
                        } else {
                            when (orientation) {
                                PORTRAIT.ordinal -> {
                                    ibAction.let {
                                        SpringAnimation(it, DynamicAnimation.TRANSLATION_Y, 0f).apply {
                                            start()
                                        }
                                        it.setImageDrawable(actionOnButtonDrawable)
                                    }
                                }
                                LANDSCAPE.ordinal -> {
                                    ibAction.let {
                                        SpringAnimation(it, DynamicAnimation.TRANSLATION_X, 0f).apply {
                                            start()
                                        }
                                        it.setImageDrawable(actionOnButtonDrawable)
                                    }
                                }
                            }
                            clContainer.background.alpha = 51
                        }
                    }
                }
                return true
            }
        })
    }

    companion object {
        val TAG = FancySwitch::class.simpleName
        const val CORNER_RADIUS = 100.0f
    }

    enum class Orientation(type: Int) {
        PORTRAIT(0), LANDSCAPE(1)
    }
}