package it.ngallazzi.fancyswitch

import android.graphics.PointF
import org.junit.Test
import org.mockito.Mockito.mock

/**
 * Local unit test for fancy switch functions
 *
 */
class FancySwitchUnitTest {
    @Test
    fun getResourceLayout_givesLayoutAccordingToLandscapeOrientation() {
        val fancySwitch = mock(FancySwitch::class.java)
        // landscape
        val orientation = FancySwitch.Orientation.LANDSCAPE
        val expectedLayout = R.layout.fancy_switch_land
        assert(fancySwitch.getResourceLayout(orientation.ordinal) == expectedLayout)
    }

    @Test
    fun getResourceLayout_givesLayoutAccordingToPortraitOrientation() {
        val fancySwitch = mock(FancySwitch::class.java)
        // landscape
        val orientation = FancySwitch.Orientation.PORTRAIT
        val expectedLayout = R.layout.fancy_switch_portrait
        assert(fancySwitch.getResourceLayout(orientation.ordinal) == expectedLayout)
    }

    @Test
    fun draggedDistance_triggersActionCompleted() {
        val fancySwitch = mock(FancySwitch::class.java)
        val actionOnCoordinates = mock(PointF::class.java)
        actionOnCoordinates.apply {
            x = 0f
            y = 0f
        }
        val actionOffCoordinates = mock(PointF::class.java)
        actionOnCoordinates.apply {
            x = 100f
            y = 0f
        }
        val draggedDistance = 100f
        assert(
            fancySwitch.isActionCompleted(
                draggedDistance,
                actionOnCoordinates,
                actionOffCoordinates,
                thresholdFactor = FancySwitch.ACTION_COMPLETED_THRESHOLD_FACTOR,
                FancySwitch.Orientation.LANDSCAPE.ordinal
            )
        )
    }

    @Test
    fun draggedDistance_triggersActionNotCompleted() {
        val fancySwitch = mock(FancySwitch::class.java)
        val actionOnCoordinates = mock(PointF::class.java)
        actionOnCoordinates.apply {
            x = 0f
            y = 0f
        }
        val actionOffCoordinates = mock(PointF::class.java)
        actionOnCoordinates.apply {
            x = 100f
            y = 0f
        }
        val draggedDistance = 10f
        assert(
            !fancySwitch.isActionCompleted(
                draggedDistance,
                actionOnCoordinates,
                actionOffCoordinates,
                thresholdFactor = FancySwitch.ACTION_COMPLETED_THRESHOLD_FACTOR,
                FancySwitch.Orientation.LANDSCAPE.ordinal
            )
        )
    }
}