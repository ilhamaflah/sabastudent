package id.saba.saba.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.saba.saba.data.models.Comment
import id.saba.saba.databinding.CardForumCommentBinding

class CommentAdapter(val data: ArrayList<Comment>, private val listener: OnCommentClickListener) :
    RecyclerView.Adapter<CommentAdapter.CommetnHolder>() {

    interface OnCommentClickListener {
        fun onVoteComment(position: Int, tipe: String)
    }

    inner class CommetnHolder(private val binding: CardForumCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            binding.txtCommentUser.text = comment.user
            binding.txtCommentWaktu.text = comment.postedTimeString()
            binding.txtCommentComment.text = comment.comment
            binding.txtCommentUpvote.text = comment.upvote.toString()
            binding.txtCommentDownvote.text = comment.downvote.toString()
            Picasso.get().load(comment.userImg).into(binding.imageViewUserKomen)

            binding.btnUpvote.setOnClickListener {
                listener.onVoteComment(absoluteAdapterPosition, "upvote")
            }

            binding.btnDownvote.setOnClickListener {
                listener.onVoteComment(absoluteAdapterPosition, "downvote")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommetnHolder {
        val binding = CardForumCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CommetnHolder(binding)
    }

    override fun onBindViewHolder(holder: CommetnHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size
}