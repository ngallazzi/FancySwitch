package it.ngallazzi.fancyswitchsample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import it.ngallazzi.fancyswitch.FancySwitch
import it.ngallazzi.fancyswitchsample.databinding.ActivityScrollingBinding

private lateinit var binding: ActivityScrollingBinding

class ScrollingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrollingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}