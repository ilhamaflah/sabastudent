package id.saba.saba.ui.tabs.home

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import id.saba.saba.HOST
import id.saba.saba.SliderModal
import id.saba.saba.data.adapters.HomeTabAdapter
import id.saba.saba.data.adapters.SliderAdapter
import id.saba.saba.data.models.User
import id.saba.saba.databinding.FragmentHomeBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import splitties.systemservices.windowManager
import splitties.toast.toast
import kotlin.math.abs

class HomeFragment : Fragment(), SliderAdapter.SliderListener {
    private var _binding: FragmentHomeBinding? = null
    private lateinit var sliderModalArrayList: ArrayList<SliderModal>
    private lateinit var adapterSlider: SliderAdapter
    private lateinit var adapterTabHome: HomeTabAdapter
    private lateinit var viewPagerTab: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var sliderJob: Job

    private val binding get() = _binding!!

    private var pos = 0

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
        viewLifecycleOwner.lifecycleScope.launch {
            loadData()
        }
    }

    private fun loadData() {
        // api call item slider, setelah dapat response, jalankan dibawah
        val queue = Volley.newRequestQueue(requireContext())
        val url = HOST().Host + "api/event"
        val stringRequest = StringRequest(Request.Method.GET, url, {
            try {
                //val arr = JSONArray(it)
                val obj = JSONObject(it)
                val arr = obj.getJSONArray("event")
                sliderModalArrayList.clear()
                for (i in 0 until arr.length()) {
                    val data = arr.getJSONObject(i)
                    val uploader = data.getJSONObject("uploader")
                    sliderModalArrayList.add(
                        SliderModal(
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
                initSlider()
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
        tabLayout = binding.tabHome

        adapterTabHome = HomeTabAdapter(childFragmentManager, lifecycle)
        viewPagerTab = binding.homeVP
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

    private fun initSlider() {
        val transformer = CompositePageTransformer()
        transformer.let {
            it.addTransformer(MarginPageTransformer(40))
            it.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.85f + (r * 0.15f)
            }
        }

        adapterSlider = SliderAdapter(sliderModalArrayList, binding.carousel, this)
        binding.carousel.let {
            it.adapter = adapterSlider
            it.offscreenPageLimit = 3
            it.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            it.setPageTransformer(transformer)
        }
        sliderJob = startSlider()
        sliderJob.start()
    }

    private fun startSlider(interval: Long = 3000): Job {
        return lifecycleScope.launch {
            while(true) {
                delay(interval)
                val currSlide = binding.carousel.currentItem

                if (currSlide >= sliderModalArrayList.size - 1) {
                    binding.carousel.currentItem = 0
                } else {
                    binding.carousel.currentItem = currSlide + 1
                }
            }
        }
    }

    override fun onSliderClick(position: Int) {
        val sliderModal = sliderModalArrayList[position]
        toast("ID: ${sliderModal.id}")
    }

    override fun onResume() {
        if (::sliderJob.isInitialized) {
            sliderJob.start()
        }
        super.onResume()
    }

    override fun onPause() {
        sliderJob.cancel()
        super.onPause()
    }
}