package id.saba.saba.ui.news

import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.saba.saba.R
import id.saba.saba.data.models.News
import id.saba.saba.data.models.User
import id.saba.saba.databinding.ActivityDetailNewsBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import splitties.toast.toast

class DetailNewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailNewsBinding
    private lateinit var news: News
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()
    }

    private fun loadData() {
        val id = intent.getIntExtra("NEWS_ID", 0)
        news = if (id != 0) {
            News(
                id,
                User(2, "User 2", "https://cdn.pixabay.com/photo/2022/01/31/12/46/bird-6983434_960_720.jpg", "username"),
                "Berita Terbaru 3",
                "https://picsum.photos/200/150",
                "11-01-2001",
                "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                        "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim " +
                        "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>",
                "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                        "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim " +
                        "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>",
                true
            )
        } else {
            intent.getParcelableExtra("NEWS")!!
        }
        initView()
    }

    private fun initView() {
        webView = binding.webViewDetailNewsContent
        binding.txtDetailNewsHeadline.text = news.judul
        binding.txtDetailNewsUser.text = news.user.username
        binding.txtDetailNewsWaktu.text = news.tanggal

        if (news.bookmarked) {
            binding.imgDetailNewsBookmark.setColorFilter(ContextCompat.getColor(this, R.color.primary), PorterDuff.Mode.SRC_IN)
        }

        webView.loadDataWithBaseURL("file:///android_asset/www", news.html(), "text/html; charset=utf-8", "UTF-8", null)
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