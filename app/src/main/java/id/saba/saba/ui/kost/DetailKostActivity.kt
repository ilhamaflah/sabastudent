package id.saba.saba.ui.kost

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.smarteist.autoimageslider.SliderView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import id.saba.saba.HOST
import id.saba.saba.R
import id.saba.saba.SliderModal
import id.saba.saba.data.adapters.SliderAdapter
import id.saba.saba.data.models.Kost
import id.saba.saba.data.models.User
import id.saba.saba.databinding.ActivityDetailKostBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import splitties.toast.toast
import java.lang.Exception

class DetailKostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailKostBinding
    private lateinit var kost: Kost
    private lateinit var viewPager: SliderView
    private lateinit var adapterSlider: SliderAdapter
    private lateinit var sliderModalArrayList: ArrayList<SliderModal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailKostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportPostponeEnterTransition()

        loadKost()
    }

    private fun loadKost() {
        kost = intent.getParcelableExtra("KOST")!!
        sliderModalArrayList = arrayListOf()
        viewPager = binding.sliderView
        val queue = Volley.newRequestQueue(this)
        val url = HOST().Host + "api/kost/" + kost.id.toString()
        val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener {
            try {
                val obj = JSONObject(it)
                val data_2 = obj.getJSONArray("gambar_kost")
                for (x in 0 until data_2.length()){
                    val data_gambar = data_2.getJSONObject(x)
                    sliderModalArrayList.add(SliderModal(x, data_gambar.getString("photo"), "Deskripsi " + x, "", User(x, "", "", ""),
                        "", "", "", "", "", "", "", "", ""))
                }
                adapterSlider = SliderAdapter(this, sliderModalArrayList)
                viewPager.setSliderAdapter(adapterSlider)
                viewPager.scrollTimeInSec = 3
                viewPager.isAutoCycle = true
            } catch (e: JSONException) {
                toast(e.message.toString())
            }
        }, Response.ErrorListener {
            toast(it.message.toString())
        })
        queue.add(stringRequest)

        initView()
    }

    private fun initView() {
        binding.imgDetailKostGambar.transitionName = "kost-${kost.id}"
        /*Picasso.get()
            .load(kost.gambar)
            .noFade()
            .into(binding.imgDetailKostGambar, object : Callback {
                override fun onSuccess() {
                    supportStartPostponedEnterTransition()
                }

                override fun onError(e: Exception?) {
                    supportStartPostponedEnterTransition()
                }

            })*/
        binding.txtDetailKostNama.text = kost.nama
        binding.txtDetailKostRating.text = kost.rating.toString()
        binding.ratingBarDetailKost.rating = kost.rating.toFloat()
        binding.txtDetailKostKategori.text = kost.kategori
        binding.txtDetailKostLokasi.text = kost.lokasi

        if (kost.boomarked) {
            binding.btnDetailKostBookmark.setColorFilter(ContextCompat.getColor(this, R.color.primary))
        }

        binding.btnDetailKostBookmark.setOnClickListener {
            kost.boomarked = !kost.boomarked
            if (kost.boomarked) {
                binding.btnDetailKostBookmark.setColorFilter(ContextCompat.getColor(this, R.color.primary), PorterDuff.Mode.SRC_IN)
            } else {
                binding.btnDetailKostBookmark.clearColorFilter()
            }
        }

        binding.btnDetailKostContact.setOnClickListener {
            makePhoneCall()
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    fun makePhoneCall(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                1
            )
        }
        else{
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:" + kost.kontak)))
        }
    }
}