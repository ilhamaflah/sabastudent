package id.saba.saba.data.adapters

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.saba.saba.ui.tabs.home.fragments.HomeEventFragment
import id.saba.saba.ui.tabs.home.fragments.HomeForumFragment
import id.saba.saba.ui.tabs.home.fragments.HomeNewsFragment

class HomeTabAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    private val fragments = arrayListOf(
        HomeNewsFragment(),
        HomeForumFragment(),
        HomeEventFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}