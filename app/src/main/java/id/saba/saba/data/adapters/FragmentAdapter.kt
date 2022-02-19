package id.saba.saba.data.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(fragment: Fragment, private val fragments: ArrayList<Fragment>) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = fragments.size
    override fun createFragment(position: Int) = fragments[position]
}