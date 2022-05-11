package id.saba.saba.ui.tabs.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.saba.saba.HOST
import id.saba.saba.data.adapters.HomeAdapter
import id.saba.saba.data.models.*
import id.saba.saba.databinding.FragmentHomeTabBinding
import id.saba.saba.ui.event.DetailEventActivity
import id.saba.saba.ui.forum.DetailForumActivity
import id.saba.saba.ui.news.DetailNewsActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import splitties.fragments.start
import splitties.toast.toast

class HomeTabFragment : Fragment(), HomeAdapter.OnHomeClickListener {
    private var _binding: FragmentHomeTabBinding? = null
    private lateinit var tab: String
    private lateinit var adapter: HomeAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var data: ArrayList<Home>

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tab = arguments?.getString("HOME_TAB")!!
        _binding = FragmentHomeTabBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        data = arrayListOf()
        adapter = HomeAdapter(data, this)
        layoutManager = LinearLayoutManager(requireContext())
        binding.dataRV.adapter = adapter
        binding.dataRV.layoutManager = layoutManager

        loadData(tab)
    }

    private fun loadData(tab: String) {
        when (tab) {
            "news" -> loadNews()
            "forum" -> loadForums()
            "event" -> loadEvents()
        }
    }

    private fun loadNews() {
        data.clear()
        val queue = Volley.newRequestQueue(requireContext())
        val url = HOST().Host + "api/berita"
        val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener {
                try {
                    //val arr = JSONArray(it)
                    val obj = JSONObject(it)
                    val arr = obj.getJSONArray("berita")
                    for (i in 0 until arr.length()) {
                        val data_obj = arr.getJSONObject(i)
                        val uploader = data_obj.getJSONObject("uploader")
                        var userad = ""
                        if(data_obj.getString("user_username") == "root"){
                            userad = "Fathul Husnan"
                        }
                        else{
                            userad = data_obj.getString("user_username")
                        }
                        data.add(
                            Home(
                                data_obj.getInt("id"),
                                User(i, uploader.getString("nama"), uploader.getString("photo"), uploader.getString("username")),
                                data_obj.getString("judul"), data_obj.getString("kutipan"),
                                false))
                        adapter.notifyItemInserted(i)
                    }
                    when (data.size){
                        0 -> adapter.notifyDataSetChanged()
                        1 -> adapter.notifyDataSetChanged()
                        else -> {
                            data = data.reversed() as ArrayList<Home>
                            adapter.notifyDataSetChanged()
                        }
                    }
                } catch (e: JSONException) {
                    toast(e.message.toString())
                }
            }, Response.ErrorListener {
                toast(it.message.toString())
            })
        queue.add(stringRequest)

        /*data.add(Home(
            1,
            User(2, "User 2", "user2@example.com"),
            "Headline News 1",
            "Deskripsi news 1",
            false
        ))

        data.add(Home(
            2,
            User(3, "User 3", "user3@example.com"),
            "Headline News 2",
            "Deskripsi news 2",
            false
        ))

        data.add(Home(
            3,
            User(1, "User 1", "user1@example.com"),
            "Headline News 3",
            "Deskripsi news 3",
            false
        ))

        data.add(Home(
            4,
            User(1, "User 1", "user1@example.com"),
            "Headline News 4",
            "Deskripsi news 4",
            false
        ))

        data.add(Home(
            5,
            User(1, "User 1", "user1@example.com"),
            "Headline News 5",
            "Deskripsi news 5",
            false
        ))

        adapter.notifyDataSetChanged()*/
    }

    private fun loadForums() {
        data.clear()
        data.add(0, Home(
            1,
            User(1, "User 1", "https://cdn.pixabay.com/photo/2022/01/31/12/46/bird-6983434_960_720.jpg", "username"),
            "Headline Forum 1",
            "Deskripsi forum 1",
            false
        ))

        data.add(1, Home(
            2,
            User(3, "User 3", "https://cdn.pixabay.com/photo/2022/01/31/12/46/bird-6983434_960_720.jpg", "username"),
            "Headline Forum 2",
            "Deskripsi forum 2",
            true
        ))

        adapter.notifyDataSetChanged()
    }

    private fun loadEvents() {
        data.clear()
        val queue = Volley.newRequestQueue(requireContext())
        val url = HOST().Host + "api/event"
        val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener {
            try {
                //val arr = JSONArray(it)
                val obj = JSONObject(it)
                val arr = obj.getJSONArray("event")
                for (i in 0 until arr.length()) {
                    val data_obj = arr.getJSONObject(i)
                    val uploader = data_obj.getJSONObject("uploader")
                    data.add(i,
                        Home(
                            data_obj.getInt("id"),
                            User(i, uploader.getString("nama"), uploader.getString("photo"), uploader.getString("username")),
                            data_obj.getString("judul"), data_obj.getString("kutipan"),
                        false
                        )
                    )
                }
                when (data.size){
                    0 -> adapter.notifyDataSetChanged()
                    1 -> adapter.notifyDataSetChanged()
                    else -> {
                        data = data.reversed() as ArrayList<Home>
                        adapter.notifyDataSetChanged()
                    }
                }
            } catch (e: JSONException) {
                toast(e.message.toString())
            }
        }, Response.ErrorListener {
            toast(it.message.toString())
        })
        queue.add(stringRequest)

        /*data.add(0, Home(
            1,
            User(1, "User 1", "user1@example.com"),
            "Headline Music Concert 1",
            "Deskripsi music concert 1",
            true
        ))

        data.add(1, Home(
            2,
            User(2, "User 2", "user2@example.com"),
            "Headline Music Concert 2",
            "Deskripsi music concert 2",
            false
        ))

        adapter.notifyDataSetChanged()*/
    }

    override fun onCardClickListener(position: Int) {
        when (tab) {
            "news" -> start<DetailNewsActivity> { this.putExtra("NEWS_ID", this@HomeTabFragment.data[position].id) }
            "forum" -> start<DetailForumActivity> { this.putExtra("FORUM_ID", this@HomeTabFragment.data[position].id) }
            "event" -> start<DetailEventActivity> { this.putExtra("EVENT_ID", this@HomeTabFragment.data[position].id) }
        }
    }

    override fun onBookmarkClickListener(position: Int) {
        //
    }
}