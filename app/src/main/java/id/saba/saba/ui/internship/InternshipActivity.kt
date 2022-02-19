package id.saba.saba.ui.internship

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.saba.saba.HOST
import id.saba.saba.data.adapters.InternshipAdapter
import id.saba.saba.data.controllers.InternshipController
import id.saba.saba.data.models.Company
import id.saba.saba.data.models.Internship
import id.saba.saba.databinding.ActivityInternshipBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import splitties.activities.start
import splitties.toast.toast

class InternshipActivity : AppCompatActivity(), InternshipAdapter.OnInternshipClickListener {
    private lateinit var binding: ActivityInternshipBinding
    private lateinit var internships: ArrayList<Internship>
    private lateinit var adapter: InternshipAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInternshipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        loadData()
    }

    private fun loadData() {
        /*internships.add(
            Internship(
                1,
                "https://picsum.photos/200/300",
                "Android Developer",
                "Deskripsi internship",
                Company(
                    1,
                    "PT. Jaya Abadi",
                    "https://ui-avatars.com/api/?name=Jaya+Abadi",
                    "Surabaya, Jawa Timur, Indonesia",
                    "+6281234567890"
                ),
                "2021-08-31",
                "Deskripsi internship"
            )
        )
        adapter.notifyItemInserted(0)

        internships.add(
            Internship(
                2,
                "https://picsum.photos/200/300",
                "iOS Developer",
                "Deskripsi internship",
                Company(
                    1,
                    "PT. Jaya Abadi",
                    "https://ui-avatars.com/api/?name=Jaya+Abadi",
                    "Surabaya, Jawa Timur, Indonesia",
                    "+6281234567890"
                ),
                "2021-08-30",
                "Deskripsi internship"
            )
        )
        adapter.notifyItemInserted(1)

        internships.add(
            Internship(
                3,
                "https://picsum.photos/200/300",
                "Fullstack Web Developer",
                "Deskripsi internship",
                Company(
                    1,
                    "PT. Jaya Abadi",
                    "https://ui-avatars.com/api/?name=Jaya+Abadi",
                    "Surabaya, Jawa Timur, Indonesia",
                    "+6281234567890"
                ),
                "2021-08-30",
                "Deskripsi internship"
            )
        )
        adapter.notifyItemInserted(2)*/
        InternshipController().listInternship(internships, adapter, this)
    }

    private fun initView() {
        internships = arrayListOf()
        adapter = InternshipAdapter(internships, this)
        layoutManager = LinearLayoutManager(this)
        binding.internshipRV.adapter = adapter
        binding.internshipRV.layoutManager = layoutManager

        binding.btnBack.setOnClickListener { finish() }
    }

    override fun onCardInternshipClickListener(position: Int) {
        start<DetailInternshipActivity> { this.putExtra("INTERNSHIP", internships[position]) }
    }
}