package id.saba.saba.ui.tabs.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.saba.saba.HOST
import id.saba.saba.data.adapters.EventAdapter
import id.saba.saba.data.models.Event
import id.saba.saba.data.models.User
import id.saba.saba.databinding.FragmentHomeEventBinding
import id.saba.saba.ui.event.DetailEventActivity
import org.json.JSONException
import org.json.JSONObject
import splitties.fragments.start
import splitties.toast.toast

class HomeEventFragment : Fragment(), EventAdapter.OnEventClickListener {
    private lateinit var binding: FragmentHomeEventBinding
    private lateinit var events: ArrayList<Event>
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeEventBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            loadEvents()
        }
    }

    private fun loadEvents() {
        val queue = Volley.newRequestQueue(requireContext())
        val url = HOST().Host + "api/event"
        val stringRequest = StringRequest(Request.Method.GET, url, {
            try {
                val obj = JSONObject(it)
                val arr = obj.getJSONArray("event")
                for (i in 0 until arr.length()) {
                    val data = arr.getJSONObject(i)
                    val uploader = data.getJSONObject("uploader")
                    events.add(
                        Event(
                            data.getInt("id"),
                            data.getString("thumbnail"),
                            data.getString("judul"), data.getString("kutipan"),
                            User(
                                i,
                                uploader.getString("nama"),
                                uploader.getString("photo"),
                                uploader.getString("username")
                            ),
                            data.getString("deskripsi"),
                            "",
                            data.getString("timestamp"),
                            data.getString("start"),
                            data.getString("end"),
                            data.getString("start"),
                            data.getString("end"),
                            data.getString("contact_person"),
                            data.getString("link_registrasi")
                        )
                    )
                }
                events.reverse()
                adapter.notifyItemRangeInserted(0, events.size)
            } catch (e: JSONException) {
                toast(e.message.toString())
            }
        }, {
            toast(it.message.toString())
        })

        queue.add(stringRequest)
    }

    private fun initView() {
        events = arrayListOf()
        adapter = EventAdapter(events, this)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.eventRV.adapter = adapter
        binding.eventRV.layoutManager = layoutManager
    }

    override fun onEventCardClickListener(position: Int) {
        start<DetailEventActivity> { this.putExtra("EVENT_ID", events[position].id) }
    }
}