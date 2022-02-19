package id.saba.saba.data.adapters

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.saba.saba.R
import id.saba.saba.data.models.Kost
import id.saba.saba.databinding.CardKostBinding

class KostAdapter(val context: Context, val data: ArrayList<Kost>, val listener: OnKostClickListener) :
    RecyclerView.Adapter<KostAdapter.KostHolder>() {

    interface OnKostClickListener {
        fun onItemClickListener(position: Int, img: ImageView)
        fun onBookmartClickListener(position: Int)
        fun onLokasiClickListener(position: Int)
    }

    inner class KostHolder(private val binding: CardKostBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            binding.cardKost.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClickListener(absoluteAdapterPosition, binding.cardKostGambar)
        }

        fun bind(kost: Kost) {
            Picasso.get().load(kost.gambar).into(binding.cardKostGambar)
            binding.cardKostNama.text = kost.nama
            binding.cardKostRating.text = kost.rating.toString()
            binding.cardKostRatingBar.rating = kost.rating.toFloat()
            binding.cardKostKategori.text = kost.kategori
            binding.cardKostLokasi.text = kost.lokasi

            ViewCompat.setTransitionName(binding.cardKostGambar, "kost-${kost.id}")

            if (kost.boomarked) {
                binding.cardKostBookmark.setColorFilter(ContextCompat.getColor(context, R.color.primary), PorterDuff.Mode.SRC_IN)
            }

            binding.cardKostBookmark.setOnClickListener {
                listener.onBookmartClickListener(absoluteAdapterPosition)
            }

            binding.cardKostLokasiLayout.setOnClickListener {
                listener.onLokasiClickListener(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KostHolder {
        val binding = CardKostBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return KostHolder(binding)
    }

    override fun onBindViewHolder(holder: KostHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size
}