package id.saba.saba.ui.scholarship

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.saba.saba.HOST
import id.saba.saba.data.adapters.NewsAdapter
import id.saba.saba.data.adapters.ScholarshipAdapter
import id.saba.saba.data.controllers.ScholarshipsController
import id.saba.saba.data.models.Company
import id.saba.saba.data.models.News
import id.saba.saba.data.models.Scholarship
import id.saba.saba.data.models.User
import id.saba.saba.databinding.ActivityScholarshipBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import splitties.activities.start
import splitties.toast.toast

class ScholarshipActivity : AppCompatActivity(), ScholarshipAdapter.OnScholarshipClickListener {
    private lateinit var binding: ActivityScholarshipBinding
    private lateinit var scholarships: ArrayList<Scholarship>
    private lateinit var adapter: ScholarshipAdapter
    private lateinit var layoutManager: LinearLayoutManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityScholarshipBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        initView()
        loadData()
    }
    
    private fun loadData() {
        /*scholarships.add(
            Scholarship(
                1,
                Company(
                    1,
                    "PT. Jaya Abadi",
                    "https://ui-avatars.com/api/?name=Jaya+Abadi",
                    "Surabaya, Jawa Timur, Indonesia",
                    "+6281234567890"
                ),
                "https://picsum.photos/200/300",
                "Scholarship 2",
                "Ringkasan scholarhip 2",
                "<h1>Content</h1>"
            )
        )
        adapter.notifyItemInserted(0)

        scholarships.add(
            Scholarship(
                2,
                Company(
                    1,
                    "PT. Jaya Abadi",
                    "https://ui-avatars.com/api/?name=Jaya+Abadi",
                    "Surabaya, Jawa Timur, Indonesia",
                    "+6281234567890"
                ),
                "https://picsum.photos/200/300",
                "Scholarship 3",
                "Ringkasan scholarhip 3",
                "<h1>Content</h1>"
            )
        )
        adapter.notifyItemInserted(1)

        scholarships.add(
            Scholarship(
                3,
                Company(
                    1,
                    "PT. Jaya Abadi",
                    "https://ui-avatars.com/api/?name=Jaya+Abadi",
                    "Surabaya, Jawa Timur, Indonesia",
                    "+6281234567890"
                ),
                "https://picsum.photos/200/300",
                "Scholarship 3",
                "Ringkasan scholarhip 3",
                "<h1>Content</h1>"
            )
        )
        adapter.notifyItemInserted(2)*/
        ScholarshipsController().listScholarship(scholarships, adapter, this)
    }

    private fun initView() {
        scholarships = arrayListOf()
        adapter = ScholarshipAdapter(scholarships, this)
        layoutManager = LinearLayoutManager(this)
        binding.scholarshipRV.adapter = adapter
        binding.scholarshipRV.layoutManager = layoutManager

        binding.btnBack.setOnClickListener { finish() }
    }

    override fun onScholarshipCardClickListener(position: Int) {
        start<DetailScholarshipActivity> { this.putExtra("SCHOLARSHIP", scholarships[position]) }
    }
}