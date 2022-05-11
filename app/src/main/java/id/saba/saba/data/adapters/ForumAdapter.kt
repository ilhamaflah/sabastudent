package id.saba.saba.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.saba.saba.data.models.Forum
import id.saba.saba.databinding.CardForumBinding

class ForumAdapter(val data: ArrayList<Forum>, private val listener: OnForumClickListener) :
    RecyclerView.Adapter<ForumAdapter.ForumHolder>() {

    interface OnForumClickListener {
        fun onForumCardClickListener(position: Int)
        fun onForumReportClickListener(position: Int)
    }

    inner class ForumHolder(private val binding: CardForumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(forum: Forum) {
            binding.txtForumHeadline.text = forum.headline
            binding.txtForumDeskripsi.text = forum.deskripsi
            binding.txtForumUser.text = forum.user.name
            Picasso.get().load(forum.user.image).into(binding.imageViewUserForum)
            binding.txtForumWaktu.text = forum.postedTimeString()
            binding.txtForumUpvote.text = forum.upvote.toString()
            binding.txtForumDownvote.text = forum.downvote.toString()
            binding.txtForumViewer.text = forum.viewer.toString()
            binding.txtForumComment.text = forum.commentCount().toString()

            binding.cardForum.setOnClickListener {
                listener.onForumCardClickListener(absoluteAdapterPosition)
            }

            binding.btnReport.setOnClickListener {
                listener.onForumReportClickListener(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumHolder {
        val binding = CardForumBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ForumHolder(binding)
    }

    override fun onBindViewHolder(holder: ForumHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size
}