package id.saba.saba.ui.scholarship

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.squareup.picasso.Picasso
import id.saba.saba.data.models.Scholarship
import id.saba.saba.databinding.ActivityDetailScholarshipBinding
import android.webkit.WebViewClient




class DetailScholarshipActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailScholarshipBinding
    private lateinit var scholarship: Scholarship
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailScholarshipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()
        initView()
    }

    private fun loadData() {
        scholarship = intent.getParcelableExtra("SCHOLARSHIP")!!
        webView = binding.webViewDetailScholarContent
    }

    private fun initView() {
        binding.txtDetailScholarTitle.text = scholarship.judul
        binding.txtDetailScholarCompLokasi.text = scholarship.company.lokasi
        binding.txtDetailScholarCompNama.text = scholarship.company.nama

        Picasso.get().load(scholarship.company.photo).into(binding.imgDetailScholarCompLogo)

        //webView.loadData(scholarship.content, "text/html; charset=utf-8", "UTF-8")
        webView.loadDataWithBaseURL("file:///android_asset/www", scholarship.html(), "text/html; charset=utf-8", "UTF-8", null)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                if (webView.progress == 100) {
                    view.requestLayout()
                }
            }
        }
    }
}