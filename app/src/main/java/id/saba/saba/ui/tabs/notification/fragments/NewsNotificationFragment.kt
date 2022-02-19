package id.saba.saba.ui.tabs.notification.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import id.saba.saba.data.adapters.NewsAdapter
import id.saba.saba.data.models.News
import id.saba.saba.data.models.User
import id.saba.saba.databinding.FragmentNotificationNewsBinding

class NewsNotificationFragment : Fragment(), NewsAdapter.OnNewsClickListener {
    private var _binding: FragmentNotificationNewsBinding? = null
    private lateinit var adapter: NewsAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var news: ArrayList<News>

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationNewsBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayNews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayNews() {
        news = arrayListOf(
            News(
                1,
                User(1, "User 1", "https://cdn.pixabay.com/photo/2022/01/31/12/46/bird-6983434_960_720.jpg", "username"),
                "Berita 1",
                "https://picsum.photos/200/150",
                "11-01-2001",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                        "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim " +
                        "eniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                        "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim " +
                        "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>",
                false
            ),
            News(
                2,
                User(1, "User 1", "https://cdn.pixabay.com/photo/2022/01/31/12/46/bird-6983434_960_720.jpg", "username"),
                "Berita 2",
                "https://picsum.photos/200/150",
                "11-01-2001",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod" +
                        " tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim " +
                        "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                        "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim " +
                        "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>",
                false
            ),
            News(
                3,
                User(2, "User 2", "https://cdn.pixabay.com/photo/2022/01/31/12/46/bird-6983434_960_720.jpg", "username"),
                "Berita 3",
                "https://picsum.photos/200/150",
                "11-01-2001",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                        "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim " +
                        "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                        "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim " +
                        "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>",
                false
            )
        )

        layoutManager = LinearLayoutManager(requireContext())
        adapter = NewsAdapter(news, this)

        binding.newsRV.layoutManager = layoutManager
        binding.newsRV.adapter = adapter
    }

    override fun onNewsCardClickListener(position: Int) {
        //
    }
}