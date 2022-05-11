package id.saba.saba.ui.forum

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import id.saba.saba.HOST
import id.saba.saba.data.adapters.CommentAdapter
import id.saba.saba.data.controllers.ForumController
import id.saba.saba.data.models.Comment
import id.saba.saba.data.models.Forum
import id.saba.saba.data.models.User
import id.saba.saba.databinding.ActivityDetailForumBinding
import id.saba.saba.databinding.DialogForumReportBinding

class DetailForumActivity : AppCompatActivity(), CommentAdapter.OnCommentClickListener {
    private lateinit var binding: ActivityDetailForumBinding
    private lateinit var dialogForumBinding: DialogForumReportBinding
    private lateinit var dialogCommentBinding: DialogForumReportBinding
    private lateinit var dialogForumReport: AlertDialog
    private lateinit var dialogCommentReport: AlertDialog
    private lateinit var forum: Forum
    private lateinit var comments: ArrayList<Comment>
    private lateinit var adapter: CommentAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailForumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadForum()
    }

    private fun loadForum() {
        // api call berdasarkan id kalau yang dikirim FORUM_ID (dari tab home)
        sharedPref = this.getSharedPreferences("SABA", Context.MODE_PRIVATE)
        val id = intent.getIntExtra("FORUM_ID", 0)
        forum = if (id != 0) {
            Forum(
                id,
                "Headline Forum 3",
                "Deskripsi forum 3",
                "https://picsum.photos/200/150",
                "2021-12-12 12:12:12",
                User(
                    1,
                    "User 1",
                    "https://cdn.pixabay.com/photo/2022/01/31/12/46/bird-6983434_960_720.jpg",
                    "username"
                ),
                234,
                23,
                2345,
                arrayListOf()
            )
        } else {
            intent.getParcelableExtra("FORUM")!!
        }
        comments = forum.comments

        initView()
    }

    private fun initView() {
        binding.txtDetailForumHeadline.text = forum.headline
        binding.txtDetailForumUser.text = forum.user.name
        binding.txtDetailForumWaktu.text = forum.tanggal
        binding.txtDetailForumDeskripsi.text = forum.deskripsi

        Picasso.get().load(forum.thumbnail).noFade().into(binding.imageViewDetailForum)
        Picasso.get().load(forum.user.image).noFade().into(binding.imageViewDetailUserAvatar)

        adapter = CommentAdapter(comments, this)
        layoutManager = LinearLayoutManager(this)
        binding.forumCommentRV.adapter = adapter
        binding.forumCommentRV.layoutManager = layoutManager

        binding.btnAddComment.setOnClickListener { addComment() }
        binding.btnBack.setOnClickListener { finish() }
        binding.btnReport.setOnClickListener { openForumReportDialog() }
    }

    private fun addComment() {
        val USER = sharedPref.getString("USER", "")
        ForumController().addKomentar(
            comments,
            adapter,
            this,
            forum.id,
            USER.toString(),
            binding.txtCommentAdd.text.toString()
        )
        binding.txtCommentAdd.setText("")
        //comments.add(comment)
        //adapter.notifyDataSetChanged()
    }

    private fun openForumReportDialog() {
        if (!this::dialogForumBinding.isInitialized) {
            dialogForumBinding = DialogForumReportBinding.inflate(layoutInflater, binding.root, false)
            dialogForumReport = MaterialAlertDialogBuilder(this)
                .setTitle("Report Forum")
                .setView(dialogForumBinding.root)
                .setPositiveButton("Report") { _, _ -> report() }
                .setNegativeButton("Cancel", null)
                .create()
        }

        dialogForumBinding.radioSpam.isChecked = true
        dialogForumBinding.txtOthers.text?.clear()
        dialogForumBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            dialogForumBinding.txtOthersLayout.visibility =
                if (checkedId == dialogForumBinding.radioOthers.id) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }

        dialogForumReport.show()
    }

    private fun openCommentReportDialog(position: Int){
        val comment = comments[position]
        if (!this::dialogCommentBinding.isInitialized) {
            dialogCommentBinding = DialogForumReportBinding.inflate(layoutInflater, binding.root, false)
            dialogCommentReport = MaterialAlertDialogBuilder(this)
                .setTitle("Report Comment")
                .setView(dialogCommentBinding.root)
                .setPositiveButton("Report") { _, _ -> reportComment(comment) }
                .setNegativeButton("Cancel", null)
                .create()
        }

        dialogCommentBinding.radioSpam.isChecked = true
        dialogCommentBinding.txtOthers.text?.clear()
        dialogCommentBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            dialogCommentBinding.txtOthersLayout.visibility =
                if (checkedId == dialogCommentBinding.radioOthers.id) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }

        dialogCommentReport.show()
    }

    override fun onCommentReportClickListener(position: Int) {
        openCommentReportDialog(position)
    }

    private fun report() {
        dialogForumReport.dismiss()
        Snackbar
            .make(binding.nSV, "Report success", Snackbar.LENGTH_SHORT)
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
                        .make(binding.nSV, "Report success", Snackbar.LENGTH_SHORT)
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
                val checkedRadioButtonId = dialogForumBinding.radioGroup.checkedRadioButtonId
                val choice = findViewById<RadioButton>(checkedRadioButtonId).text.toString()

                // atur params yang mau dikirim ke server
                return if (checkedRadioButtonId == dialogForumBinding.radioOthers.id) {
                    val info = dialogForumBinding.txtOthers.text.toString()

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

    private fun reportComment(comment: Comment) {
        dialogCommentReport.dismiss()
        Snackbar
            .make(binding.nSV, "Report success", Snackbar.LENGTH_SHORT)
            .show()
        /*val queue = Volley.newRequestQueue(this)
        val url = "${HOST().Host}/api/forum/${comment.id}/report"
        val request = object : StringRequest(
            Method.POST,
            url,
            { res ->
                try {
                    // kalau success report
                    dialogReport.dismiss()
                    Snackbar
                        .make(binding.layoutComment, "Report success", Snackbar.LENGTH_SHORT)
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
                val checkedRadioButtonId = dialogCommentBinding.radioGroup.checkedRadioButtonId
                val choice = findViewById<RadioButton>(checkedRadioButtonId).text.toString()

                // atur params yang mau dikirim ke server
                return if (checkedRadioButtonId == dialogCommentBinding.radioOthers.id) {
                    val info = dialogCommentBinding.txtOthers.text.toString()

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

    override fun onVoteComment(position: Int, tipe: String) {
        when (tipe) {
            "upvote" -> comments[position].upvote++
            "downvote" -> comments[position].downvote++
        }
        adapter.notifyItemChanged(position)
    }
}