package id.saba.saba.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.saba.saba.data.models.Home
import id.saba.saba.databinding.CardHomeBinding

class HomeAdapter(val data: ArrayList<Home>, val listener: OnHomeClickListener) : RecyclerView.Adapter<HomeAdapter.HomeHolder>() {
    interface OnHomeClickListener {
        fun onCardClickListener(position: Int)
        fun onBookmarkClickListener(position: Int)
    }

    inner class HomeHolder(val binding: CardHomeBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cardHome.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onCardClickListener(absoluteAdapterPosition)
        }

        fun bind(home: Home) {
            binding.cardHomeJudul.text = home.judul
            binding.cardHomeUsername.text = home.user.username
            binding.cardHomeKutipan.text = home.kutipan

            binding.cardHomeBookmark.setOnClickListener {
                listener.onBookmarkClickListener(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.HomeHolder {
        val binding = CardHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return HomeHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeAdapter.HomeHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size
}