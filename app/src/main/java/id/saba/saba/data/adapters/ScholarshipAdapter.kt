package id.saba.saba.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.saba.saba.data.models.Scholarship
import id.saba.saba.databinding.CardScholarshipBinding

class ScholarshipAdapter(private val data: ArrayList<Scholarship>, val listener: OnScholarshipClickListener) : RecyclerView.Adapter<ScholarshipAdapter.ScholarshipHolder>() {
    interface OnScholarshipClickListener {
        fun onScholarshipCardClickListener(position: Int)
    }

    inner class ScholarshipHolder(val binding: CardScholarshipBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.cardScholarship.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onScholarshipCardClickListener(absoluteAdapterPosition)
        }

        fun bind(scholarship: Scholarship) {
            Picasso.get().load(scholarship.thumbnail).into(binding.cardScholarshipImg)
            binding.cardScholarshipTitle.text = scholarship.judul
            binding.cardScholarshipDeskripsi.text = scholarship.kutipan
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScholarshipHolder {
        val binding = CardScholarshipBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ScholarshipHolder(binding)
    }

    override fun onBindViewHolder(holder: ScholarshipHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size
}