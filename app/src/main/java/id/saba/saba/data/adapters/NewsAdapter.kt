package id.saba.saba.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.saba.saba.data.models.News
import id.saba.saba.databinding.CardNewsBinding

class NewsAdapter(private val data: ArrayList<News>, private val listener: OnNewsClickListener) :
    RecyclerView.Adapter<NewsAdapter.NewsHolder>() {

    interface OnNewsClickListener {
        fun onNewsCardClickListener(position: Int)
    }

    inner class NewsHolder(private val binding: CardNewsBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.cardNews.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onNewsCardClickListener(absoluteAdapterPosition)
        }

        fun bind(news: News) {
            binding.cardNewsJudul.text = news.judul
            binding.cardNewsUser.text = news.user.username
            binding.cardNewsDeskripsi.text = news.kutipan
            binding.cardNewsTanggal.text = news.tanggal
            Picasso.get().load(news.thumbnail).into(binding.cardNewsImg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val binding = CardNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NewsHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size
}