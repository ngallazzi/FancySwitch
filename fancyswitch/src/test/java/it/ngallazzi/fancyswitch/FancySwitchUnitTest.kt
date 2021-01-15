package it.ngallazzi.fancyswitch

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Test

/**
 * Local unit test for fancy switch functions
 *
 */
class FancySwitchUnitTest {
    val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun getResourceLayout_givesLayoutAccordingToOrientation() {
        // landscape
        val orientation = FancySwitch.Orientation.LANDSCAPE
        val expectedLayout = R.layout.fancy_switch_land
    }
}