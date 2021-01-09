package it.ngallazzi.fancyswitchsample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import it.ngallazzi.fancyswitch.FancySwitch
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fsLock.setState(FancySwitch.State.ON)
        fsLock.setSwitchStateChangedListener(object : FancySwitch.SwitchStateChangedListener {
            override fun onChanged(newState: FancySwitch.State) {
                when (newState){
                    FancySwitch.State.ON ->  Toast.makeText(this@MainActivity,
                        "New switch state: Locked", Toast.LENGTH_SHORT).show()
                    FancySwitch.State.OFF ->  Toast.makeText(this@MainActivity,
                        "New switch state: Unlocked", Toast.LENGTH_SHORT).show()
                }

                when (newState) {
                    FancySwitch.State.ON ->  fsSmile.setState(FancySwitch.State.ON)
                    FancySwitch.State.OFF ->  fsSmile.setState(FancySwitch.State.OFF)
                }
            }
        })

        fsSmile.setSwitchStateChangedListener(object : FancySwitch.SwitchStateChangedListener{
            override fun onChanged(newState: FancySwitch.State) {
                when (newState){
                    FancySwitch.State.ON ->  Toast.makeText(this@MainActivity,
                        "Smile, life is great!", Toast.LENGTH_SHORT).show()
                    FancySwitch.State.OFF ->  Toast.makeText(this@MainActivity,
                        "Sorry you had a bad day", Toast.LENGTH_SHORT).show()
                }
            }

        })

        fsHdr.setSwitchStateChangedListener(object : FancySwitch.SwitchStateChangedListener{
            override fun onChanged(newState: FancySwitch.State) {
                when (newState){
                    FancySwitch.State.ON ->  Toast.makeText(this@MainActivity,
                        "Hdr is ON", Toast.LENGTH_SHORT).show()
                    FancySwitch.State.OFF ->  Toast.makeText(this@MainActivity,
                        "Hdr is OFF", Toast.LENGTH_SHORT).show()
                }

            }

        })

        fsEye.setSwitchStateChangedListener(object : FancySwitch.SwitchStateChangedListener{
            override fun onChanged(newState: FancySwitch.State) {
                when (newState){
                    FancySwitch.State.ON ->  Toast.makeText(this@MainActivity,
                        "Visibility is ON", Toast.LENGTH_SHORT).show()
                    FancySwitch.State.OFF ->  Toast.makeText(this@MainActivity,
                        "Visibility is OFF", Toast.LENGTH_SHORT).show()
                }
            }

        })

        fsAlarmClock.setSwitchStateChangedListener(object : FancySwitch.SwitchStateChangedListener{
            override fun onChanged(newState: FancySwitch.State) {
                when (newState){
                    FancySwitch.State.ON ->  Toast.makeText(this@MainActivity,
                        "Alarm clock ON", Toast.LENGTH_SHORT).show()
                    FancySwitch.State.OFF ->  Toast.makeText(this@MainActivity,
                        "Alarm clock OFF", Toast.LENGTH_SHORT).show()
                }
            }

        })

        fsCamera.setSwitchStateChangedListener(object : FancySwitch.SwitchStateChangedListener{
            override fun onChanged(newState: FancySwitch.State) {
                when (newState){
                    FancySwitch.State.ON ->  Toast.makeText(this@MainActivity,
                        "Front camera selected", Toast.LENGTH_SHORT).show()
                    FancySwitch.State.OFF ->  Toast.makeText(this@MainActivity,
                        "Back camera selected", Toast.LENGTH_SHORT).show()
                }
            }

        })


        val mainLooperHandler = Handler(Looper.getMainLooper())

        mainLooperHandler.post(object : Runnable {
            override fun run() {
                when (fsCamera.currentState){
                    FancySwitch.State.ON -> fsCamera.setState(FancySwitch.State.OFF)
                    else -> fsCamera.setState(FancySwitch.State.ON)
                }
                mainLooperHandler.postDelayed(this, 2000)
            }
        })
    }
}
