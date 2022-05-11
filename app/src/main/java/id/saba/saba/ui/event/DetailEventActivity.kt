package id.saba.saba.ui.event

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Picasso
import id.saba.saba.data.models.Event
import id.saba.saba.data.models.User
import id.saba.saba.databinding.ActivityDetailEventBinding
import splitties.toast.toast

class DetailEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEventBinding
    private lateinit var event: Event

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()
    }

    private fun loadData() {
        val id = intent.getIntExtra("EVENT_ID", 0)
        event = if (id != 0) {
            Event(
                id,
                "https://picsum.photos/200/300",
                "Music Concert",
                "Headline Music Concert 1",
                User(1, "User 1", "https://images.unsplash.com/photo-1499242249421-ac7daa30e504?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80", "username"),
                "Deskripsi music concert 1",
                "Universitas Ciputra, Jawa Timur, Surabaya",
                "2021-08-24",
                "2021-08-27",
                "2021-09-1",
                "2021-08-27 12:06:00",
                "2021-09-1 18:00:00",
                "00000",
                "http://www.sabastudent.com"
            )
        } else {
            intent.getParcelableExtra("EVENT")!!
        }
        initView()
    }

    private fun initView() {
        BottomSheetBehavior.from(binding.sheetDetailEvent).apply {
            peekHeight = 128
            state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.imgDetailEventGambar.transitionName = "event-${event.id}"

        binding.txtDetailEventTitle.text = event.judul
        //binding.txtDetailEventHeadline.text = event.headline
        binding.txtDetailEventDeskripsi.text = event.deskripsi
        binding.txtEventDetailTanggal.text = event.textTanggal()
        binding.txtEventDetailWaktu.text = event.textWaktu()
        //binding.btnEventDetailEarly.text = event.textEarly()
        //binding.btnEventDetailRegular.text = event.textRegular()
        binding.txtEventDetailLokasi.text = event.lokasi

        binding.btnEventDetailContact.setOnClickListener { makePhoneCall() }
        binding.btnEventDetailRegistrasi.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(event.link_registrasi)
            startActivity(intent)
        }
        binding.btnBack.setOnClickListener { finish() }

        Picasso.get().load(event.gambar).noFade()
            .into(binding.imgDetailEventGambar)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall()
            } else {
                toast("Permission DENIED")
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun makePhoneCall() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 101)
        } else {
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:" + event.contact_person)))
        }
    }
}