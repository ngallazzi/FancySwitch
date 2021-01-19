package it.ngallazzi.fancyswitchsample

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import it.ngallazzi.fancyswitch.FancyState
import it.ngallazzi.fancyswitch.FancySwitch
import it.ngallazzi.fancyswitchsample.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.fsSmile.setSwitchStateChangedListener(object : FancySwitch.StateChangedListener {
            override fun onChanged(newState: FancyState) {
                when (newState.id) {
                    FancyState.State.ON -> Toast.makeText(
                        this@MainActivity,
                        "Smile, life is great!", Toast.LENGTH_SHORT
                    ).show()
                    FancyState.State.OFF -> Toast.makeText(
                        this@MainActivity,
                        "Sorry you had a bad day", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        binding.btChangeActivity.setOnClickListener {
            val intent = Intent(this, ScrollingActivity::class.java)
            startActivity(intent)
        }

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            binding.fsAlarmClock.setState(FancyState.State.ON)
        }, 2000)
    }
}