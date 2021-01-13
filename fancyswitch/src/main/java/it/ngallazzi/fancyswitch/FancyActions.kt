package it.ngallazzi.fancyswitch

interface FancyActions {
    fun setState(newState: FancyState)
    fun setSwitchStateChangedListener(listener: FancySwitch.StateChangedListener)
}