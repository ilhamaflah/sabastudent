package id.saba.saba.ui.event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.saba.saba.HOST
import id.saba.saba.R
import id.saba.saba.data.adapters.EventAdapter
import id.saba.saba.data.controllers.EventController
import id.saba.saba.data.models.Event
import id.saba.saba.data.models.User
import id.saba.saba.databinding.ActivityEventBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import splitties.activities.start
import splitties.intents.start
import splitties.toast.toast

class EventActivity : AppCompatActivity(), EventAdapter.OnEventClickListener {
    private lateinit var binding: ActivityEventBinding
    private lateinit var events: ArrayList<Event>
    private lateinit var adapter: EventAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initView()
        //loadData()
    }

    private fun loadData() {
        // pas dapat response dari volley, response di loop lalu array event kosong tadi ditambah
        /*events.add(Event(
            1,
            "https://picsum.photos/200/300",
            "Music Concert",
            "Headline Music Concert 1",
            User(1, "User 1", "user1@example.com"),
            "Deskripsi music concert 1",
            "Universitas Ciputra, Jawa Timur, Surabaya",
            "2021-08-24",
            "2021-08-27",
            12,
            15,
            250000,
            350000
        ))
        adapter.notifyItemInserted(0) // pakai index dari loop

        events.add(Event(
            2,
            "https://picsum.photos/200/300",
            "Music Concert",
            "Headline Music Concert 2",
            User(2, "User 2", "user2@example.com"),
            "Deskripsi music concert 2",
            "Universitas Surabaya, Jawa Timur, Surabaya",
            "2021-08-20",
            "2021-08-25",
            15,
            18,
            200000,
            250000
        ))
        adapter.notifyItemInserted(1)

        // reverse array untuk menampilkan data terbaru (dihapus kalau dari server udah diurut)
        events = events.reversed() as ArrayList<Event>*/
        /*val queue = Volley.newRequestQueue(this)
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
                            User(i, uploader.getString("nama"), uploader.getString("photo"), uploader.getString("username")),
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
                    adapter.notifyItemInserted(i)
                }
            } catch (e: JSONException) {
                toast(e.message.toString())
            }
        }, {
            toast(it.message.toString())
        })
        queue.add(stringRequest)*/
        EventController().listEvent(events, adapter, this)
    }

    private fun initView() {
        // karena volley itu async, events dibikin array kosongan
        events = arrayListOf()
        adapter = EventAdapter(events, this)
        layoutManager = LinearLayoutManager(this)
        binding.eventRV.adapter = adapter
        binding.eventRV.layoutManager = layoutManager
        binding.eventRV.addItemDecoration(DividerItemDecoration(binding.eventRV.context, layoutManager.orientation))

        binding.actionButton.setOnClickListener {
            start<CreateEventActivity>()
        }
    }

    override fun onEventCardClickListener(position: Int) {
        start<DetailEventActivity> { this.putExtra("EVENT", events[position]) }
    }

    override fun onResume() {
        super.onResume()

        initView()
        loadData()
    }
}