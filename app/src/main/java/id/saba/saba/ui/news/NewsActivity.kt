package id.saba.saba.ui.news

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import id.saba.saba.data.adapters.NewsListAdapter
import id.saba.saba.databinding.ActivityNewsBinding
import id.saba.saba.ui.form.FormActivity
import splitties.activities.start

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var adapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        viewPager = binding.newsVP
        tabLayout = binding.tabsNews
        adapter = NewsListAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Latest"
                1 -> tab.text = "Popular"
            }
        }.attach()

        binding.btnBack.setOnClickListener { finish() }
        binding.actionButton.setOnClickListener {
            start<FormActivity> { this.putExtra("FORM", "news") }
        }
    }
}