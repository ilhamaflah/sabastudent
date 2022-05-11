package id.saba.saba.ui.tabs.home.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import id.saba.saba.HOST
import id.saba.saba.data.adapters.ForumAdapter
import id.saba.saba.data.models.Comment
import id.saba.saba.data.models.Forum
import id.saba.saba.data.models.User
import id.saba.saba.databinding.DialogForumReportBinding
import id.saba.saba.databinding.FragmentHomeForumBinding
import id.saba.saba.ui.forum.DetailForumActivity
import org.json.JSONException
import org.json.JSONObject
import splitties.fragments.start
import splitties.toast.toast

class HomeForumFragment : Fragment(), ForumAdapter.OnForumClickListener {
    private lateinit var binding: FragmentHomeForumBinding
    private lateinit var forums: ArrayList<Forum>
    private lateinit var adapter: ForumAdapter
    private lateinit var dialogBinding: DialogForumReportBinding
    private lateinit var dialogReport: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeForumBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            loadEvents()
        }
    }

    private fun loadEvents() {
        val sharedPref = requireActivity().getSharedPreferences("SABA", Context.MODE_PRIVATE)
        val username = sharedPref.getString("USER", "")
        val queue = Volley.newRequestQueue(requireContext())
        val url = HOST().Host + "api/forum"
        val stringRequest = object : StringRequest(
            Method.POST,
            url,
            {
                try {
                    val obj = JSONObject(it)
                    val arr = obj.getJSONArray("forum")
                    for (i in 0 until arr.length()) {
                        val comments = ArrayList<Comment>()
                        val data = arr.getJSONObject(i)
                        val uploader = data.getJSONObject("uploader")
                        if (data.has("komentar_forum")) {
                            for (x in 0 until data.getJSONArray("komentar_forum").length()) {
                                val data_2 = data.getJSONArray("komentar_forum").getJSONObject(x)
                                comments.add(
                                    Comment(
                                        data_2.getInt("id"),
                                        data_2.getString("userKomen"),
                                        data_2.getString("userKomenPic"),
                                        data_2.getString("timestamp"),
                                        data_2.getString("komentar"),
                                        0,
                                        0
                                    )
                                )
                            }

                        }
                        forums.add(
                            Forum(
                                data.getInt("id"),
                                data.getString("judul"),
                                data.getString("deskripsi"),
                                data.getString("thumbnail"),
                                data.getString("timestamp"),
                                User(
                                    i,
                                    uploader.getString("nama"),
                                    uploader.getString("photo"),
                                    uploader.getString("username")
                                ),
                                data.getInt("total_like"), 100, 101, comments
                            )
                        )
                        //adapter.notifyItemInserted(i)
                    }
                    when (forums.size) {
                        0 -> adapter.notifyDataSetChanged()
                        1 -> adapter.notifyDataSetChanged()
                        else -> {
                            forums.reverse()
                            adapter.notifyDataSetChanged()
                        }
                    }
                } catch (e: JSONException) {
                    toast(e.message.toString())
                }
            },
            {
                toast(it.message.toString())
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return HashMap<String, String>().apply {
                    put("user_username", username!!)
                }
            }
        }
        queue.add(stringRequest)
    }

    private fun initView() {
        forums = arrayListOf()
        adapter = ForumAdapter(forums, this)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.forumRV.adapter = adapter
        binding.forumRV.layoutManager = layoutManager
    }

    override fun onForumCardClickListener(position: Int) {
        start<DetailForumActivity> { this.putExtra("FORUM_ID", forums[position].id) }
    }

    override fun onForumReportClickListener(position: Int) {
        openReportDialog(position)
    }

    private fun openReportDialog(position: Int) {
        val forum = forums[position]
        if (!this::dialogBinding.isInitialized) {
            dialogBinding = DialogForumReportBinding.inflate(layoutInflater, binding.root, false)
            dialogReport = MaterialAlertDialogBuilder(requireContext())
                .setTitle("Report Forum")
                .setView(dialogBinding.root)
                .setPositiveButton("Report") { _, _ -> report(forum) }
                .setNegativeButton("Cancel", null)
                .create()
        }

        dialogBinding.radioSpam.isChecked = true
        dialogBinding.txtOthers.text?.clear()
        dialogBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            dialogBinding.txtOthersLayout.visibility =
                if (checkedId == dialogBinding.radioOthers.id) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }

        dialogReport.show()
    }

    private fun report(forum: Forum) {
        val queue = Volley.newRequestQueue(requireContext())
        val url = "${HOST().Host}/api/forum/${forum.id}/report"
        val request = object : StringRequest(
            Method.POST,
            url,
            { res ->
                try {
                    // kalau success report
                    dialogReport.dismiss()
                    Snackbar
                        .make(binding.layoutForum, "Report success", Snackbar.LENGTH_SHORT)
                        .show()
                } catch (e: Exception) {
                    Log.d("ERR_RES", e.message.toString())
                }
            },
            { err ->
                Log.d("ERR", err.message.toString())
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val sharedPref = requireActivity().getSharedPreferences("SABA", Context.MODE_PRIVATE)
                val user = sharedPref.getString("USER", "")
                val checkedRadioButtonId = dialogBinding.radioGroup.checkedRadioButtonId
                val choice = binding.root.rootView.findViewById<RadioButton>(checkedRadioButtonId).text.toString()

                // atur params yang mau dikirim ke server
                return if (checkedRadioButtonId == dialogBinding.radioOthers.id) {
                    val info = dialogBinding.txtOthers.text.toString()

                    hashMapOf(
                        "user" to user!!,
                        "choice" to choice,
                        "info" to info
                    )
                } else {
                    hashMapOf(
                        "user" to user!!,
                        "choice" to choice,
                    )
                }
            }
        }

        queue.add(request)
    }
}