package id.saba.saba.ui.news.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.saba.saba.HOST
import id.saba.saba.data.adapters.NewsAdapter
import id.saba.saba.data.controllers.NewsController
import id.saba.saba.data.models.News
import id.saba.saba.data.models.User
import id.saba.saba.databinding.FragmentNewsListBinding
import id.saba.saba.ui.news.DetailNewsActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import splitties.fragments.start
import splitties.toast.toast

class NewsListFragment : Fragment(), NewsAdapter.OnNewsClickListener {
    private var _binding: FragmentNewsListBinding? = null
    private lateinit var adapter: NewsAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var news: ArrayList<News>
    private lateinit var tipe: String

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tipe = arguments?.getString("NEWS_TYPE")!!
        _binding = FragmentNewsListBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun loadData(tipeNews: String) {
        news = arrayListOf()
        if (tipeNews === "popular") {
            news.add(
                News(
                    1,
                    User(1, "User 1", "https://cdn.pixabay.com/photo/2022/01/31/12/46/bird-6983434_960_720.jpg", "username"),
                    "Berita Populer 1",
                    "https://picsum.photos/200/150",
                    "11-01-2001",
                    "<img src='https://picsum.photos/200/150' width='100%' />" +
                            "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                            "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim " +
                            "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>",
                    "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                            "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim " +
                            "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>",
                    false
                )
            )
            adapter.notifyItemInserted(0)

            news.add(
                News(
                    2,
                    User(1, "User 1", "https://cdn.pixabay.com/photo/2022/01/31/12/46/bird-6983434_960_720.jpg", "username"),
                    "Berita Populer 2",
                    "https://picsum.photos/200/150",
                    "11-01-2001",
                    "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                            "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim " +
                            "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>",
                    "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                            "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim " +
                            "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>",
                    false
                )
            )
            adapter.notifyItemInserted(1)

            news.add(
                News(
                    3,
                    User(2, "User 2", "https://cdn.pixabay.com/photo/2022/01/31/12/46/bird-6983434_960_720.jpg", "username"),
                    "Berita Populer 3",
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
            )
            adapter.notifyItemInserted(2)
            news = news.reversed() as ArrayList<News>
            adapter = NewsAdapter(news, this)
            layoutManager = LinearLayoutManager(requireContext())
            binding.newsRV.adapter = adapter
            binding.newsRV.layoutManager = layoutManager
        } else {
            // api call berita terbaru
            adapter = NewsAdapter(news, this)
            layoutManager = LinearLayoutManager(requireContext())
            binding.newsRV.adapter = adapter
            binding.newsRV.layoutManager = layoutManager
            NewsController().listNews(news, adapter, requireContext())
            /*news.add(
                News(
                    1,
                    User(1, "User 1", "user1@example.com"),
                    "Berita Terbaru 1",
                    "https://picsum.photos/200/150",
                    "11-01-2001",
                    "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                            "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim " +
                            "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>",
                    true
                )
            )
            adapter.notifyItemInserted(0)

            news.add(
                News(
                    2,
                    User(1, "User 1", "user1@example.com"),
                    "Berita Terbaru 2",
                    "https://picsum.photos/200/150",
                    "11-01-2001",
                    "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                            "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim " +
                            "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>",
                    false
                )
            )
            adapter.notifyItemInserted(1)

            news.add(
                News(
                    3,
                    User(2, "User 2", "user2@example.com"),
                    "Berita Terbaru 3",
                    "https://picsum.photos/200/150",
                    "11-01-2001",
                    "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                            "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim " +
                            "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>",
                    true
                )
            )*/
            //adapter.notifyItemInserted(2)
        }
        //news = news.reversed() as ArrayList<News>
    }

    private fun initView() {
        news = arrayListOf()
        adapter = NewsAdapter(news, this)
        layoutManager = LinearLayoutManager(requireContext())
        binding.newsRV.adapter = adapter
        binding.newsRV.layoutManager = layoutManager

        loadData(tipe)
    }

    override fun onNewsCardClickListener(position: Int) {
        start<DetailNewsActivity> { this.putExtra("NEWS", news[position]) }
    }
}