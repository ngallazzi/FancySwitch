package it.ngallazzi.fancyswitchsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import it.ngallazzi.fancyswitch.FancySwitch
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fancySwitch_1.setSwitchStateChangedListener(object : FancySwitch.SwitchStateChangedListener {
            override fun onChanged(newState: FancySwitch.State) {
                Toast.makeText(this@MainActivity, "New switch state: ${newState.name}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
