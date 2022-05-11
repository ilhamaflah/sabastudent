package id.saba.saba.ui.forum

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.core.util.rangeTo
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import id.saba.saba.HOST
import id.saba.saba.data.adapters.ForumAdapter
import id.saba.saba.data.controllers.ForumController
import id.saba.saba.data.models.Comment
import id.saba.saba.data.models.Forum
import id.saba.saba.data.models.User
import id.saba.saba.databinding.ActivityForumBinding
import id.saba.saba.databinding.DialogForumReportBinding
import id.saba.saba.ui.form.FormActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import splitties.activities.start
import splitties.toast.toast
import kotlin.collections.ArrayList

class ForumActivity : AppCompatActivity(), ForumAdapter.OnForumClickListener {
    private lateinit var forums: ArrayList<Forum>
    private lateinit var binding: ActivityForumBinding
    private lateinit var adapter: ForumAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var sharedPref: SharedPreferences
    private lateinit var dialogBinding: DialogForumReportBinding
    private lateinit var dialogReport: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        loadData()
    }

    private fun loadData() {
        /*forums.add(Forum(
            1,
            "Headline Forum 1",
            "Deskripsi forum 1",
            "2021-08-15 20:13:53",
            User(1, "User 1", "user1@example.com"),
            321,
            21,
            1234,
            arrayListOf(
                Comment(
                    1,
                    "User 2",
                    "2021-08-15 22:11:03",
                    "Comment 1",
                    123,
                    12
                )
            )
        ))
        adapter.notifyItemInserted(0)

        forums.add(Forum(
            2,
            "Headline Forum 2",
            "Deskripsi forum 2",
            "2021-08-15 22:11:35",
            User(2, "User 2", "user2@example.com"),
            234,
            23,
            2345,
            arrayListOf(
                Comment(
                    2,
                    "User 1",
                    "2021-08-15 23:21:06",
                    "Comment 2",
                    23,
                    2
                )
            )
        ))
        adapter.notifyItemInserted(1)*/

        /*layoutManager = LinearLayoutManager(this)
        adapter = ForumAdapter(forums, this)
        binding.forumRV.adapter = adapter
        binding.forumRV.layoutManager = layoutManager*/
        sharedPref = this.getSharedPreferences("SABA", Context.MODE_PRIVATE)
        val USER = sharedPref.getString("USER", "")
        ForumController().listForum(forums, adapter, USER.toString(),this)
    }

    private fun initView() {
        forums = arrayListOf()
        layoutManager = LinearLayoutManager(this)
        adapter = ForumAdapter(forums, this)
        binding.forumRV.adapter = adapter
        binding.forumRV.layoutManager = layoutManager

        binding.actionButton.setOnClickListener {
            start<FormActivity> { this.putExtra("FORM", "forum") }
        }
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun addForum() {
        val forum = Forum(
            3,
            "Headline Forum 3",
            "Deskripsi forum 3",
            "https://picsum.photos/200/150",
            "2021-08-17 23:21:06",
            User(1, "User 1", "https://cdn.pixabay.com/photo/2022/01/31/12/46/bird-6983434_960_720.jpg", "username"),
            234,
            23,
            2345,
            arrayListOf()
        )
        forums.add(0, forum)
        adapter.notifyItemInserted(0)
        layoutManager.smoothScrollToPosition(binding.forumRV, null, 0)
    }

    override fun onForumCardClickListener(position: Int) {
        start<DetailForumActivity> { this.putExtra("FORUM", forums[position]) }
    }

    override fun onForumReportClickListener(position: Int) {
        openReportDialog(position)
    }

    private fun openReportDialog(position: Int) {
        val forum = forums[position]
        if (!this::dialogBinding.isInitialized) {
            dialogBinding = DialogForumReportBinding.inflate(layoutInflater, binding.root, false)
            dialogReport = MaterialAlertDialogBuilder(this)
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
        dialogReport.dismiss()
        Snackbar
            .make(binding.layoutForum, "Report success", Snackbar.LENGTH_SHORT)
            .show()
        /*val queue = Volley.newRequestQueue(this)
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
                val user = sharedPref.getString("USER", "")
                val checkedRadioButtonId = dialogBinding.radioGroup.checkedRadioButtonId
                val choice = findViewById<RadioButton>(checkedRadioButtonId).text.toString()

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

        queue.add(request)*/
    }
}