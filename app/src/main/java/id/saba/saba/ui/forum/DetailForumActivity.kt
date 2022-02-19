package id.saba.saba.ui.forum

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import id.saba.saba.data.adapters.CommentAdapter
import id.saba.saba.data.controllers.ForumController
import id.saba.saba.data.models.Comment
import id.saba.saba.data.models.Forum
import id.saba.saba.data.models.User
import id.saba.saba.databinding.ActivityDetailForumBinding
import splitties.views.material.text

class DetailForumActivity : AppCompatActivity(), CommentAdapter.OnCommentClickListener {
    private lateinit var binding: ActivityDetailForumBinding
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
                User(1, "User 1", "https://cdn.pixabay.com/photo/2022/01/31/12/46/bird-6983434_960_720.jpg", "username"),
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
    }

    private fun addComment() {
        val USER = sharedPref.getString("USER", "")
        ForumController().addKomentar(comments, adapter, this, forum.id, USER.toString(), binding.txtCommentAdd.text.toString())
        binding.txtCommentAdd.setText("")
        //comments.add(comment)
        //adapter.notifyDataSetChanged()
    }

    override fun onVoteComment(position: Int, tipe: String) {
        when (tipe) {
            "upvote" -> comments[position].upvote++
            "downvote" -> comments[position].downvote++
        }
        adapter.notifyItemChanged(position)
    }
}