package it.ngallazzi.fancyswitchsample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import it.ngallazzi.fancyswitch.FancySwitch
import it.ngallazzi.fancyswitchsample.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.fsLock.setState(FancySwitch.State.ON)
        binding.fsLock.setSwitchStateChangedListener(object : FancySwitch.SwitchStateChangedListener {
            override fun onChanged(newState: FancySwitch.State) {
                when (newState) {
                    FancySwitch.State.ON -> Toast.makeText(
                        this@MainActivity,
                        "New switch state: Locked", Toast.LENGTH_SHORT
                    ).show()
                    FancySwitch.State.OFF -> Toast.makeText(
                        this@MainActivity,
                        "New switch state: Unlocked", Toast.LENGTH_SHORT
                    ).show()
                }

                when (newState) {
                    FancySwitch.State.ON -> binding.fsSmile.setState(FancySwitch.State.ON)
                    FancySwitch.State.OFF -> binding.fsSmile.setState(FancySwitch.State.OFF)
                }
            }
        })

        binding.fsSmile.setSwitchStateChangedListener(object : FancySwitch.SwitchStateChangedListener {
            override fun onChanged(newState: FancySwitch.State) {
                when (newState) {
                    FancySwitch.State.ON -> Toast.makeText(
                        this@MainActivity,
                        "Smile, life is great!", Toast.LENGTH_SHORT
                    ).show()
                    FancySwitch.State.OFF -> Toast.makeText(
                        this@MainActivity,
                        "Sorry you had a bad day", Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })

        binding.fsHdr.setSwitchStateChangedListener(object : FancySwitch.SwitchStateChangedListener {
            override fun onChanged(newState: FancySwitch.State) {
                when (newState) {
                    FancySwitch.State.ON -> Toast.makeText(
                        this@MainActivity,
                        "Hdr is ON", Toast.LENGTH_SHORT
                    ).show()
                    FancySwitch.State.OFF -> Toast.makeText(
                        this@MainActivity,
                        "Hdr is OFF", Toast.LENGTH_SHORT
                    ).show()
                }

            }

        })

        binding.fsEye.setSwitchStateChangedListener(object : FancySwitch.SwitchStateChangedListener {
            override fun onChanged(newState: FancySwitch.State) {
                when (newState) {
                    FancySwitch.State.ON -> Toast.makeText(
                        this@MainActivity,
                        "Visibility is ON", Toast.LENGTH_SHORT
                    ).show()
                    FancySwitch.State.OFF -> Toast.makeText(
                        this@MainActivity,
                        "Visibility is OFF", Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })

        binding.fsAlarmClock.setSwitchStateChangedListener(object : FancySwitch.SwitchStateChangedListener {
            override fun onChanged(newState: FancySwitch.State) {
                when (newState) {
                    FancySwitch.State.ON -> Toast.makeText(
                        this@MainActivity,
                        "Alarm clock ON", Toast.LENGTH_SHORT
                    ).show()
                    FancySwitch.State.OFF -> Toast.makeText(
                        this@MainActivity,
                        "Alarm clock OFF", Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })

        binding.fsCamera.setSwitchStateChangedListener(object : FancySwitch.SwitchStateChangedListener {
            override fun onChanged(newState: FancySwitch.State) {
                when (newState) {
                    FancySwitch.State.ON -> Toast.makeText(
                        this@MainActivity,
                        "Front camera selected", Toast.LENGTH_SHORT
                    ).show()
                    FancySwitch.State.OFF -> Toast.makeText(
                        this@MainActivity,
                        "Back camera selected", Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })


        val mainLooperHandler = Handler(Looper.getMainLooper())

        mainLooperHandler.postDelayed(object : Runnable {
            override fun run() {
                when (binding.fsCamera.currentState) {
                    FancySwitch.State.ON -> binding.fsCamera.setState(FancySwitch.State.OFF)
                    else -> binding.fsCamera.setState(FancySwitch.State.ON)
                }
                when (binding.fsLock.currentState) {
                    FancySwitch.State.ON -> binding.fsLock.setState(FancySwitch.State.OFF)
                    else -> binding.fsLock.setState(FancySwitch.State.ON)
                }
                mainLooperHandler.postDelayed(this, 3000)
            }
        }, 3000)
    }
}
