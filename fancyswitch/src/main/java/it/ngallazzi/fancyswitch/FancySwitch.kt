package it.ngallazzi.fancyswitch

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import it.ngallazzi.fancyswitch.FancySwitch.Orientation.LANDSCAPE
import it.ngallazzi.fancyswitch.FancySwitch.Orientation.PORTRAIT
import kotlinx.android.synthetic.main.fancy_switch_portrait.view.*
import kotlin.math.abs


/**
 * SwipeToUnlock
 * Created by Nicola on 2/1/2019.
 * Copyright © 2019 Nicola Gallazzi. All rights reserved.
 */
class FancySwitch @kotlin.jvm.JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), FancyActions {

    private val stateOn: FancyState
    private val stateOff: FancyState

    private lateinit var currentState: FancyState

    private lateinit var changeListener: StateChangedListener

    private val actionButtonMargin: Int
    private val mBaseColor: Int
    private val orientation: Int
    private val springAnimation: SpringAnimation

    private var ivActionOnPosition = PointF(0f, 0f)

    private var ivActionOffPosition = PointF(0f, 0f)

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

        val actionOnDrawable = attributes.getDrawable(R.styleable.FancySwitch_actionOnDrawable)
            ?: ContextCompat.getDrawable(context, R.drawable.ic_lock_closed)!!

        val actionOffDrawable = attributes.getDrawable(R.styleable.FancySwitch_actionOffDrawable)
            ?: ContextCompat.getDrawable(context, R.drawable.ic_lock_open)!!

        stateOn = FancyState(FancyState.State.ON, actionOnDrawable)
        stateOff = FancyState(FancyState.State.OFF, actionOffDrawable)

        val backgroundShape = GradientDrawable()
        backgroundShape.shape = GradientDrawable.RECTANGLE
        backgroundShape.cornerRadius = CORNER_RADIUS
        backgroundShape.setColor(mBaseColor)

        inflateLayout(orientation)

        clContainer.background = backgroundShape

        ivActionOff.setImageDrawable(stateOff.drawable)

        ivActionOn.setImageDrawable(stateOn.drawable)

        springAnimation = initAnimation(orientation)

        setState(stateOff.id)
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
                    DynamicAnimation.TRANSLATION_Y, ANIMATION_INITIAL_POSITION
                )
            else -> SpringAnimation(
                ibAction,
                DynamicAnimation.TRANSLATION_X, ANIMATION_INITIAL_POSITION
            )
        }
        animation.spring.stiffness = SpringForce.STIFFNESS_LOW
        return animation
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        ivActionOnPosition.apply {
            x = ivActionOn.x
            y = ivActionOn.y
        }

        ivActionOffPosition.apply {
            x = ivActionOff.x
            y = ivActionOff.y
        }

        springAnimation.animateToFinalPosition(
            getFinalPositionForState(
                currentState,
                ivActionOnPosition,
                ivActionOffPosition
            )
        )

        clContainer.background.alpha = currentState.id.alpha

        ibAction.apply {
            val currentStateDrawableCopy = currentState.drawable.constantState!!.newDrawable()
            currentStateDrawableCopy.setColorFilter(mBaseColor, PorterDuff.Mode.SRC_ATOP)
            setImageDrawable(currentStateDrawableCopy)

            setOnTouchListener { v, event ->
                val actionButton = v as ImageButton
                actionButton.parent.requestDisallowInterceptTouchEvent(true)

                when (event.action) {
                    // release
                    MotionEvent.ACTION_UP -> {
                        val draggedDistance =
                            getDraggedDistance(event, ivActionOnPosition, ivActionOffPosition)
                        Log.v(TAG, "Distance: $draggedDistance")
                        if (isActionCompleted(
                                draggedDistance,
                                ivActionOnPosition,
                                ivActionOffPosition
                            )
                        ) {
                            when (currentState) {
                                stateOn -> setState(stateOff.id)
                                stateOff -> setState(stateOn.id)
                            }
                        } else {
                            when (currentState) {
                                stateOn -> setState(stateOn.id)
                                stateOff -> setState(stateOff.id)
                            }
                        }
                    }
                }
                true
            }
        }
    }

    private fun isActionCompleted(
        draggedDistance: Float,
        actionOnCoordinates: PointF,
        actionOffCoordinates: PointF
    ): Boolean {
        return when (orientation) {
            PORTRAIT.ordinal -> draggedDistance >= ((actionOffCoordinates.y
                    - actionOnCoordinates.y) / ACTION_COMPLETED_THRESHOLD_FACTOR)
            LANDSCAPE.ordinal -> draggedDistance >= ((actionOnCoordinates.x
                    - actionOffCoordinates.x) / ACTION_COMPLETED_THRESHOLD_FACTOR)
            else -> false
        }
    }

    private fun getDraggedDistance(
        event: MotionEvent,
        actionOnCoordinates: PointF,
        actionOffCoordinates: PointF
    ): Float {
        return when (currentState) {
            stateOn -> {
                when (orientation) {
                    PORTRAIT.ordinal -> abs(event.y - actionOnCoordinates.y)
                    LANDSCAPE.ordinal -> abs(event.x - actionOnCoordinates.x)
                    else -> ANIMATION_INITIAL_POSITION
                }
            }

            stateOff -> {
                when (orientation) {
                    PORTRAIT.ordinal -> abs(event.y - actionOffCoordinates.y)
                    LANDSCAPE.ordinal -> abs(event.x - actionOffCoordinates.x)
                    else -> ANIMATION_INITIAL_POSITION
                }
            }
            else -> 0f
        }
    }

    private fun getFinalPositionForState(
        state: FancyState,
        actionOnCoordinates: PointF,
        actionOffCoordinates: PointF
    ): Float {
        return when (state) {
            stateOn -> {
                when (orientation) {
                    PORTRAIT.ordinal ->
                        -(actionOffCoordinates.y - actionOnCoordinates.y - actionButtonMargin)
                    else ->
                        (actionOnCoordinates.x - actionOffCoordinates.x - actionButtonMargin)
                }
            }
            stateOff -> {
                when (orientation) {
                    PORTRAIT.ordinal -> ANIMATION_INITIAL_POSITION
                    else -> ANIMATION_INITIAL_POSITION
                }
            }
            else -> 0f
        }
    }


    override fun setState(newState: FancyState.State) {
        currentState = when (newState) {
            FancyState.State.ON -> stateOn
            FancyState.State.OFF -> stateOff
        }

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
        const val ANIMATION_INITIAL_POSITION = 0.0f
        const val ACTION_COMPLETED_THRESHOLD_FACTOR = 5.0f
    }

    enum class Orientation {
        PORTRAIT, LANDSCAPE
    }

    interface StateChangedListener {
        fun onChanged(newState: FancyState)
    }
}