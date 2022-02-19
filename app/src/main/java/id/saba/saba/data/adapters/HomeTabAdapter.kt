package id.saba.saba.data.adapters

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.saba.saba.ui.tabs.home.HomeTabFragment

class HomeTabAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): HomeTabFragment {
        val fragment = HomeTabFragment()
        val bundle = Bundle()

        when (position) {
            0 -> bundle.putString("HOME_TAB", "news")
            1 -> bundle.putString("HOME_TAB", "forum")
            2 -> bundle.putString("HOME_TAB", "event")
        }

        fragment.arguments = bundle

        return fragment
    }
}