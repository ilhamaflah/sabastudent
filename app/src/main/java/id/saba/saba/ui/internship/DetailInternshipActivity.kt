package id.saba.saba.ui.internship

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.squareup.picasso.Picasso
import id.saba.saba.data.models.Internship
import id.saba.saba.databinding.ActivityDetailInternshipBinding

class DetailInternshipActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailInternshipBinding
    private lateinit var internship: Internship
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailInternshipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()
        initView()
    }

    private fun loadData() {
        internship = intent.getParcelableExtra("INTERNSHIP")!!
    }

    private fun initView() {
        binding.txtDetailInternPosition.text = internship.judul
        binding.txtDetailInternCompNama.text = internship.company.nama
        binding.txtDetailInternCompLokasi.text = internship.company.lokasi
        binding.txtDetailInternTanggal.text = internship.tanggal
        Picasso.get().load(internship.company.photo).into(binding.imgDetailInternCompLogo)

        webView = binding.webViewDetailInternship
        webView.loadDataWithBaseURL("file:///android_asset/www", internship.html(), "text/html; charset=utf-8", "UTF-8", null)
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                if (webView.progress == 100) {
                    view.requestLayout()
                }
            }
        }
        webView.settings.javaScriptEnabled = true
    }
}