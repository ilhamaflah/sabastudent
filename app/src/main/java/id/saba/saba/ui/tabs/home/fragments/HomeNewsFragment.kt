package id.saba.saba.ui.tabs.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.saba.saba.HOST
import id.saba.saba.data.adapters.NewsAdapter
import id.saba.saba.data.models.News
import id.saba.saba.data.models.User
import id.saba.saba.databinding.FragmentHomeNewsBinding
import id.saba.saba.ui.news.DetailNewsActivity
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import splitties.fragments.start
import splitties.toast.toast

class HomeNewsFragment : Fragment(), NewsAdapter.OnNewsClickListener {
    private lateinit var binding: FragmentHomeNewsBinding
    private lateinit var news: ArrayList<News>
    private lateinit var adapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeNewsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            loadNews()
        }
    }

    private fun loadNews() {
        val queue = Volley.newRequestQueue(requireContext())
        val url = HOST().Host + "api/berita"
        val stringRequest = StringRequest(
            url,
            {
                try {
                    val obj = JSONObject(it)
                    val arr = obj.getJSONArray("berita")
                    for (i in 0 until arr.length()) {
                        val data = arr.getJSONObject(i)
                        val uploader = data.getJSONObject("uploader")
                        var userad = if (data.getString("user_username") == "root") {
                            "Fathul Husnan"
                        } else {
                            data.getString("user_username")
                        }
                        news.add(
                            News(
                                data.getInt("id"),
                                User(
                                    i,
                                    uploader.getString("nama"),
                                    uploader.getString("photo"),
                                    uploader.getString("username")
                                ),
                                data.getString("judul"),
                                data.getString("thumbnail"),
                                data.getString("timestamp"),
                                data.getString("kutipan"),
                                data.getString("deskripsi"),
                            )
                        )
                        adapter.notifyItemInserted(i)
                    }
                } catch (e: JSONException) {
                    toast(e.message.toString())
                }
            },
            {
                toast(it.message.toString())
            }
        )

        queue.add(stringRequest)
    }

    private fun initView() {
        news = arrayListOf()
        adapter = NewsAdapter(news, this)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.newsRV.adapter = adapter
        binding.newsRV.layoutManager = layoutManager
    }

    override fun onNewsCardClickListener(position: Int) {
        start<DetailNewsActivity> { this.putExtra("NEWS", news[position]) }
    }
}