package it.ngallazzi.fancyswitch

enum class FancyState(
    val alpha: Int
) {
    ON(NO_ALPHA),
    OFF(OFF_ALPHA);
}

const val NO_ALPHA = 255
const val OFF_ALPHA = 51
