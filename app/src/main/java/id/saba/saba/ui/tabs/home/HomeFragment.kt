package id.saba.saba.ui.tabs.home

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.smarteist.autoimageslider.SliderView
import id.saba.saba.HOST
import id.saba.saba.R
import id.saba.saba.SliderModal
import id.saba.saba.data.adapters.HomeTabAdapter
import id.saba.saba.data.adapters.SliderAdapter
import id.saba.saba.data.models.User
import id.saba.saba.databinding.FragmentHomeBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import splitties.systemservices.windowManager
import splitties.toast.toast

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewPager: SliderView
    private lateinit var adapterSlider: SliderAdapter
    private lateinit var sliderModalArrayList: ArrayList<SliderModal>
    private lateinit var adapterTabHome: HomeTabAdapter
    private lateinit var viewPagerTab: ViewPager2
    private lateinit var tabLayout: TabLayout

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        _binding!!.layoutContent.layoutParams.height = metrics.heightPixels - 230

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        loadData()
    }

    private fun loadData() {
        // api call item slider, setelah dapat response, jalankan dibawah
        viewPager = binding.idViewPager
        val queue = Volley.newRequestQueue(requireContext())
        val url = HOST().Host + "api/event"
        val stringRequest = StringRequest(Request.Method.GET, url, {
            try {
                //val arr = JSONArray(it)
                val obj = JSONObject(it)
                val arr = obj.getJSONArray("event")
                for (i in 0 until arr.length()) {
                    val data = arr.getJSONObject(i)
                    val uploader = data.getJSONObject("uploader")
                    sliderModalArrayList.add(
                        SliderModal(
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
                            data.getString("link_registrasi"))
                    )
                    adapterSlider = SliderAdapter(requireContext(), sliderModalArrayList)
                    viewPager.setSliderAdapter(adapterSlider)
                    viewPager.scrollTimeInSec = 3
                    viewPager.isAutoCycle = true
                }
            } catch (e: JSONException) {
                toast(e.message.toString())
            }
        }, {
            toast(it.message.toString())
        })
        queue.add(stringRequest)
    }

    private fun initViews() {
        sliderModalArrayList = arrayListOf()
        viewPagerTab = binding.homeVP
        tabLayout = binding.tabHome
        adapterTabHome = HomeTabAdapter(parentFragmentManager, lifecycle)
        viewPagerTab.adapter = adapterTabHome

        TabLayoutMediator(tabLayout, viewPagerTab) { tab, position ->
            when (position) {
                0 -> tab.text = "News"
                1 -> tab.text = "Forum"
                2 -> tab.text = "Event"
            }
        }.attach()

        val inputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.parentLayout.setOnClickListener {
            binding.inputSearch.clearFocus()
            inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }
    }
}