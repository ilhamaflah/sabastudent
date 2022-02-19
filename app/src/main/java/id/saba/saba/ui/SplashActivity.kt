package id.saba.saba.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import id.saba.saba.MainActivity
import id.saba.saba.data.models.User
import id.saba.saba.databinding.ActivitySplashBinding
import id.saba.saba.ui.auth.LoginActivity
import splitties.activities.start

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var sharedPref: SharedPreferences
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)

        sharedPref = this.getSharedPreferences("SABA", Context.MODE_PRIVATE)

        when (sharedPref.getString("USER", "")) {
            "" -> {
                setContentView(binding.root)
                binding.btnGetStarted.setOnClickListener { start<LoginActivity>() }
            }
            else -> start<MainActivity>()
        }
    }
}