package id.saba.saba.ui.tabs.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import id.saba.saba.R
import id.saba.saba.data.adapters.FragmentAdapter
import id.saba.saba.ui.tabs.notification.fragments.EventNotificationFragment
import id.saba.saba.ui.tabs.notification.fragments.ForumNotificationFragment
import id.saba.saba.ui.tabs.notification.fragments.NewsNotificationFragment
import kotlinx.android.synthetic.main.fragment_notification.view.*

class NotificationFragment : Fragment() {
    private lateinit var adapter: FragmentAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
       return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //        displayTabLayout(view)
    }

    private fun displayTabLayout(v: View) {
        val fragments = arrayListOf(NewsNotificationFragment(), ForumNotificationFragment(), EventNotificationFragment())
        adapter = FragmentAdapter(this, fragments)
        val tabLayout = v.tabLayout
        val homeVP = v.homeVP

        homeVP.adapter = adapter
        TabLayoutMediator(tabLayout, homeVP) { tab, position ->
            when (position) {
                0 -> tab.text = "News"
                1 -> tab.text = "Forum"
                2 -> tab.text = "Event"
            }
        }.attach()
    }
}