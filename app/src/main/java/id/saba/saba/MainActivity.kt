package id.saba.saba

import android.Manifest
import android.app.UiModeManager
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import id.saba.saba.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.saba.saba.data.controllers.AdsController
import id.saba.saba.databinding.BottomSheetCategoryBinding
import id.saba.saba.ui.event.EventActivity
import id.saba.saba.ui.internship.InternshipActivity
import id.saba.saba.ui.kost.KostActivity
import id.saba.saba.ui.news.NewsActivity
import id.saba.saba.ui.scholarship.ScholarshipActivity
import splitties.activities.start

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var uiModeManager : UiModeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        uiModeManager = getSystemService(UI_MODE_SERVICE) as UiModeManager
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)
        binding.fabMenu.setOnClickListener { bottomSheetDialog.show() }
        makeGpsOn()
        nightModeOff()
        initCategory()

        /*// Initialize the Mobile Ads SDK.
        MobileAds.initialize(this) {}

        // Set your test devices. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
        // to get test ads on this device."
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("ABCDEF012345"))
                .build()
        )*/
        AdsController(this, "MainActivity", this).showInterstitial()
    }

    private fun initCategory() {
        val bottomSheetBinding = BottomSheetCategoryBinding.inflate(layoutInflater)
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.cardForum.setOnClickListener {
            //bottomSheetDialog.dismiss()
            //start<ForumActivity>()
        }

        bottomSheetBinding.cardKost.setOnClickListener {
            bottomSheetDialog.dismiss()
            start<KostActivity>()
        }

        bottomSheetBinding.cardNews.setOnClickListener {
            bottomSheetDialog.dismiss()
            start<NewsActivity>()
        }

        bottomSheetBinding.cardEvents.setOnClickListener {
            bottomSheetDialog.dismiss()
            start<EventActivity>()
        }

        bottomSheetBinding.cardInternships.setOnClickListener {
            bottomSheetDialog.dismiss()
            start<InternshipActivity>()
        }

        bottomSheetBinding.cardScholarships.setOnClickListener {
            bottomSheetDialog.dismiss()
            start<ScholarshipActivity>()
        }
    }

    fun makeGpsOn(){
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            }
            else{

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun nightModeOff(){
        uiModeManager.nightMode = UiModeManager.MODE_NIGHT_NO
    }
}