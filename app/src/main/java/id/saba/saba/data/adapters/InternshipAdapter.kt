package id.saba.saba.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.saba.saba.data.models.Internship
import id.saba.saba.databinding.CardInternshipBinding

class InternshipAdapter(
    private val data: ArrayList<Internship>,
    private val listener: OnInternshipClickListener
) : RecyclerView.Adapter<InternshipAdapter.InternshipHolder>() {
    interface OnInternshipClickListener {
        fun onCardInternshipClickListener(position: Int)
    }

    inner class InternshipHolder(val binding: CardInternshipBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cardInternship.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onCardInternshipClickListener(absoluteAdapterPosition)
        }

        fun bind(internship: Internship) {
            Picasso.get().load(internship.thumbnail).into(binding.cardInternshipImg)
            binding.cardInternshipTitle.text = internship.judul
            binding.cardInternshipDeskripsi.text = internship.kutipan
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InternshipHolder {
        val binding =
            CardInternshipBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return InternshipHolder(binding)
    }

    override fun onBindViewHolder(holder: InternshipHolder, position: Int) =
        holder.bind(data[position])

    override fun getItemCount(): Int = data.size
}