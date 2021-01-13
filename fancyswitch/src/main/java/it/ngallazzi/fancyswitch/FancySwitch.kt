package it.ngallazzi.fancyswitch

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import it.ngallazzi.fancyswitch.FancySwitch.Orientation.LANDSCAPE
import it.ngallazzi.fancyswitch.FancySwitch.Orientation.PORTRAIT
import kotlinx.android.synthetic.main.fancy_switch_portrait.view.clContainer
import kotlinx.android.synthetic.main.fancy_switch_portrait.view.ibAction
import kotlinx.android.synthetic.main.fancy_switch_portrait.view.ivActionOff
import kotlinx.android.synthetic.main.fancy_switch_portrait.view.ivActionOn
import kotlin.math.abs


/**
 * SwipeToUnlock
 * Created by Nicola on 2/1/2019.
 * Copyright © 2019 Zehus. All rights reserved.
 */
class FancySwitch @kotlin.jvm.JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), FancyActions {

    private var backgroundShape = GradientDrawable()
    private var actionOnDrawable: Drawable
    private var actionOnButtonDrawable: Drawable
    private var actionOffDrawable: Drawable
    private var actionOffButtonDrawable: Drawable

    var currentState: FancyState = FancyState.OFF

    private lateinit var changeListener: StateChangedListener

    private val actionButtonMargin: Int
    private val mBaseColor: Int
    private val orientation: Int
    private val springAnimation: SpringAnimation

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

        actionButtonMargin =
            ((resources.getDimension(R.dimen.iv_action_completed_margin) /
                    resources.displayMetrics.density).toInt())

        actionOnDrawable = attributes.getDrawable(R.styleable.FancySwitch_actionOnDrawable)
        actionOffDrawable = attributes.getDrawable(R.styleable.FancySwitch_actionOffDrawable)
        actionOnButtonDrawable = actionOnDrawable.constantState.newDrawable().mutate()
        actionOffButtonDrawable = actionOffDrawable.constantState.newDrawable().mutate()

        backgroundShape.shape = GradientDrawable.RECTANGLE
        backgroundShape.cornerRadius = CORNER_RADIUS
        backgroundShape.setColor(mBaseColor)

        inflateLayout(orientation)

        clContainer.background = backgroundShape

        ivActionOff.setImageDrawable(actionOffDrawable)
        ivActionOn.setImageDrawable(actionOnDrawable)

        DrawableCompat.setTint(actionOnButtonDrawable, mBaseColor)
        ibAction.setImageDrawable(actionOnButtonDrawable)
        DrawableCompat.setTint(actionOffButtonDrawable, mBaseColor)

        springAnimation = initAnimation(orientation)

        setState(currentState)
    }

    private fun inflateLayout(orientation: Int) {
        when (orientation) {
            PORTRAIT.ordinal -> inflate(context, R.layout.fancy_switch_portrait, this)
            LANDSCAPE.ordinal -> inflate(context, R.layout.fancy_switch_land, this)
        }
    }

    private fun initAnimation(orientation: Int): SpringAnimation {
        val animation = when (orientation) {
            PORTRAIT.ordinal ->
                SpringAnimation(
                    ibAction,
                    DynamicAnimation.TRANSLATION_Y, 0f
                )
            else -> SpringAnimation(
                ibAction,
                DynamicAnimation.TRANSLATION_X, 0f
            )
        }
        animation.spring.stiffness = SpringForce.STIFFNESS_LOW
        return animation
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        var translationDelta = 0f

        ibAction.setOnTouchListener { v, event ->
            val actionButton = v as ImageButton
            actionButton.parent.requestDisallowInterceptTouchEvent(true)

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    when (orientation) {
                        PORTRAIT.ordinal -> translationDelta = actionButton.y - event.y
                        LANDSCAPE.ordinal -> translationDelta = actionButton.x - event.x
                    }
                }

                // movement
                MotionEvent.ACTION_MOVE -> {
                    when (orientation) {
                        PORTRAIT.ordinal -> {
                            /*if (event.y + translationDelta in ivActionOn.y..ivActionOff.y) {
                                actionButton.animate()
                                    .y(event.y + translationDelta)
                                    .start()
                            }*/
                        }
                        LANDSCAPE.ordinal -> {
                            /*if (event.x + translationDelta in ivActionOff.x..ivActionOn.x) {
                                actionButton.animate()
                                    .x(event.x + translationDelta)
                                    .start()
                            }*/
                        }
                    }
                }
                // release
                MotionEvent.ACTION_UP -> {
                    val draggedDistance = getDraggedDistance(event)
                    Log.v(TAG, "Distance: $draggedDistance")
                    if (isActionCompleted(draggedDistance)) {
                        when (currentState) {
                            FancyState.ON -> setState(FancyState.OFF)
                            FancyState.OFF -> setState(FancyState.ON)
                        }
                    } else {
                        when (currentState) {
                            FancyState.ON -> setState(FancyState.ON)
                            FancyState.OFF -> setState(FancyState.OFF)
                        }
                    }
                }
            }
            true
        }
    }

    private fun isActionCompleted(draggedDistance: Float): Boolean {
        return when (orientation) {
            PORTRAIT.ordinal -> draggedDistance >= ((ivActionOff.y - ivActionOn.y) / 5.0f)
            LANDSCAPE.ordinal -> draggedDistance >= ((ivActionOn.x - ivActionOff.x) / 5.0f)
            else -> false
        }
    }

    private fun getDraggedDistance(event: MotionEvent): Float {
        return when (currentState) {
            FancyState.ON -> {
                when (orientation) {
                    PORTRAIT.ordinal -> abs(event.y - ivActionOn.y)
                    LANDSCAPE.ordinal -> abs(event.x - ivActionOn.x)
                    else -> 0.0f
                }
            }
            FancyState.OFF -> {
                when (orientation) {
                    PORTRAIT.ordinal -> abs(event.y - ivActionOff.y)
                    LANDSCAPE.ordinal -> abs(event.x - ivActionOff.x)
                    else -> 0.0f
                }
            }
        }
    }

    private fun getFinalPositionForState(state: FancyState): Float {
        return when (state) {
            FancyState.ON -> {
                when (orientation) {
                    PORTRAIT.ordinal -> -(ivActionOff.y - ivActionOn.y - actionButtonMargin)
                    else -> ivActionOn.x - ivActionOff.x - actionButtonMargin
                }
            }
            FancyState.OFF -> {
                when (orientation) {
                    PORTRAIT.ordinal -> 0f
                    else -> 0f
                }
            }
        }
    }


    override fun setState(newState: FancyState) {
        when (newState) {
            FancyState.ON -> ibAction.setImageDrawable(actionOnButtonDrawable)
            FancyState.OFF -> ibAction.setImageDrawable(actionOffButtonDrawable)
        }

        springAnimation.animateToFinalPosition(getFinalPositionForState(newState))
        clContainer.background.alpha = newState.alpha
        currentState = newState
        requestLayout()

        if (::changeListener.isInitialized) {
            changeListener.onChanged(currentState)
        }
    }

    override fun setSwitchStateChangedListener(listener: StateChangedListener) {
        changeListener = listener
    }

    companion object {
        val TAG = FancySwitch::class.simpleName
        const val CORNER_RADIUS = 100.0f
    }

    enum class Orientation {
        PORTRAIT, LANDSCAPE
    }

    interface StateChangedListener {
        fun onChanged(newState: FancyState)
    }
}