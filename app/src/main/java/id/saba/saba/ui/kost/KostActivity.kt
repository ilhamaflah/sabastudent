package id.saba.saba.ui.kost

import android.Manifest
import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import id.saba.saba.GpsTracker
import id.saba.saba.HOST
import id.saba.saba.data.adapters.KostAdapter
import id.saba.saba.data.controllers.KostController
import id.saba.saba.data.models.Kost
import id.saba.saba.databinding.ActivityKostBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import splitties.toast.toast

class KostActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    KostAdapter.OnKostClickListener {
    private lateinit var binding: ActivityKostBinding
    private lateinit var fm: FragmentManager
    private lateinit var map: GoogleMap
    private lateinit var kosts: ArrayList<Kost>
    private lateinit var adapter: KostAdapter
    private lateinit var sheet: BottomSheetBehavior<NestedScrollView>
    private var gpsTracker =  GpsTracker(this)
    private var LATITUDEINPUT : String = ""
    private var LONGITUDEINPUT : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityKostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getLocation()
        gpsTracker.stopUsingGPS()
        initView()
    }

    private fun loadData() {
        val mapFragment = SupportMapFragment.newInstance()
        fm = supportFragmentManager
        fm.beginTransaction()
            .add(binding.fragmentView.id, mapFragment)
            .commit()
        mapFragment.getMapAsync(this)
        KostController().listKost(kosts, adapter, this)
        /*kosts.add(
            Kost(
                1,
                "https://picsum.photos/200/300",
                "Kos 123",
                4.32,
                "Kos Laki-laki",
                "Universitas Ciputra, Jawa Timur, Indonesia",
                LatLng(-7.2874004, 112.6314373),
                false
            )
        )
        adapter.notifyItemInserted(0)
        kosts.add(
            Kost(
                2,
                "https://picsum.photos/200/300",
                "Kos 321",
                4.76,
                "Kos Cewe",
                "Universitas Ciputra, Jawa Timur, Indonesia",
                LatLng(-7.2890247, 112.6327868),
                false
            )
        )
        adapter.notifyItemInserted(1)*/
    }

    private fun initView() {
        sheet = BottomSheetBehavior.from(binding.sheetListKost).apply {
            peekHeight = 128
            state = BottomSheetBehavior.STATE_COLLAPSED
//            window.setDimAmount(0F)
//            window.setBackgroundDrawableResource(android.R.color.transparent)
//            window.setBackgroundDrawable(null)
//            binding.sheetListKost.background = null
//            binding.sheetListKost.setBackgroundColor(Color.TRANSPARENT)

        }

        binding.btnBack.setOnClickListener { finish() }

        kosts = arrayListOf()
        adapter = KostAdapter(this, kosts, this)
        binding.kostRV.adapter = adapter
        binding.kostRV.layoutManager = (object : LinearLayoutManager(this, HORIZONTAL, false) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                lp.width = (width / 2.3).toInt()
                return true
            }
        })

        // load data setelah layout
        loadData()
        /*val mapFragment = SupportMapFragment.newInstance()
        fm = supportFragmentManager
        fm.beginTransaction()
            .add(binding.fragmentView.id, mapFragment)
            .commit()
        mapFragment.getMapAsync(this)*/
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        map.setPadding(0, 0, 0, 128)
        val builder = LatLngBounds.Builder()

        // tambah marker
        kosts.forEachIndexed { index, kost ->
            builder.include(kost.koordinat)
            val marker = map.addMarker(
                MarkerOptions().position(kost.koordinat).title(kost.nama)
            )
            marker?.tag = index
        }

        // biar map langsung zoom ke area semua marker
        val bounds = builder.build()
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        val padding = (height * 0.2).toInt()
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
        map.animateCamera(cu)
        map.setOnMarkerClickListener(this)

        /*with(map) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(LATITUDEINPUT.toDouble(), LONGITUDEINPUT.toDouble()), 15f))
        }*/
    }

    override fun onItemClickListener(position: Int, img: ImageView) {
        val intent = Intent(this, DetailKostActivity::class.java)
        intent.putExtra("KOST", kosts[position])
        /*intent.putExtra("KOST_TRANSITION", ViewCompat.getTransitionName(img))

        val options = ActivityOptions.makeSceneTransitionAnimation(this, img, ViewCompat.getTransitionName(img))

        startActivity(intent, options.toBundle())*/
        startActivity(intent)
    }

    override fun onBookmartClickListener(position: Int) {
        kosts[position].boomarked = !kosts[position].boomarked
        adapter.notifyItemChanged(position)
    }

    override fun onLokasiClickListener(position: Int) {
        val cameraPosition = CameraPosition.Builder()
            .target(kosts[position].koordinat)
            .zoom(18F)
            .build()
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        sheet.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        binding.kostRV.layoutManager?.scrollToPosition(marker.tag.toString().toInt())
        sheet.state = BottomSheetBehavior.STATE_EXPANDED
        return false
    }

    private fun getLocation() {
        gpsTracker = GpsTracker(this)
        if (gpsTracker.canGetLocation()) {
            LONGITUDEINPUT = gpsTracker.getLongitude().toString()
            LATITUDEINPUT = gpsTracker.getLatitude().toString()
            Log.d("getLocation", LATITUDEINPUT + " " + LONGITUDEINPUT)
        } else {
            gpsTracker.showSettingsAlert()
        }
    }
}