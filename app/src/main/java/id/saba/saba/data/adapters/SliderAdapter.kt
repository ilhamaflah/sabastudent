package id.saba.saba.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.squareup.picasso.Picasso
import id.saba.saba.SliderModal
import id.saba.saba.databinding.SliderLayoutBinding

class SliderAdapter(
    private val data: ArrayList<SliderModal>,
    val viewPager: ViewPager2,
    private val listener: SliderListener
) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    interface SliderListener {
        fun onSliderClick(position: Int)
    }

    inner class SliderViewHolder(private val binding: SliderLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.idRLSlider.setOnClickListener(this)
        }

        fun bind(slider: SliderModal) {
            Picasso.get().load(slider.img).into(binding.idIV)
        }

        override fun onClick(v: View?) {
            listener.onSliderClick(absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding =
            SliderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) =
        holder.bind(data[position])

    override fun getItemCount() = data.size
}