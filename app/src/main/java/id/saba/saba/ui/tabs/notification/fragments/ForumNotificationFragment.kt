package id.saba.saba.ui.tabs.notification.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import id.saba.saba.ClassTrendingForum
import id.saba.saba.R

class ForumNotificationFragment : Fragment() {
    private lateinit var forums: ArrayList<ClassTrendingForum>
//    private lateinit var adapter: TrendingForumAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification_forum, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        displayTrendingForum(view)
    }

//    private fun displayTrendingForum(root: View) {
//        forums = arrayListOf(
//            ClassTrendingForum(
//                "Pengguna A",
//                "11-01-2001",
//                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
//            ),
//            ClassTrendingForum(
//                "Pengguna B",
//                "11-01-2001",
//                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
//            ),
//            ClassTrendingForum(
//                "Pengguna C",
//                "11-01-2001",
//                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
//            )
//        )
//        val forumsAdapter = TrendingForumAdapter(forums.reversed()) { forums ->
//            run {
//                toast(forums.name)
//                // start<DetailForumActivity>()
//            }
//        }
//        root.forumRV.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = forumsAdapter
//        }
//    }
}