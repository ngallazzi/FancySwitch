package it.ngallazzi.fancyswitch

import android.graphics.drawable.Drawable

class FancyState(
    val id: State,
    val drawable: Drawable
) {
    enum class State(val alpha: Int) {
        ON(NO_ALPHA), OFF(OFF_ALPHA)
    }
}


const val NO_ALPHA = 255
const val OFF_ALPHA = 51
