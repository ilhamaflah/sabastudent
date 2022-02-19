package id.saba.saba.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.saba.saba.data.models.Event
import id.saba.saba.databinding.CardEventBinding

class EventAdapter(val data: ArrayList<Event>, val listener: OnEventClickListener) : RecyclerView.Adapter<EventAdapter.EventHolder>() {

    interface OnEventClickListener {
        fun onEventCardClickListener(position: Int)
    }

    inner class EventHolder(val binding: CardEventBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.cardEvent.setOnClickListener(this)
        }

        fun bind(event: Event) {
            Picasso.get().load(event.gambar).into(binding.cardEventImg)
            binding.cardEventTitle.text = event.judul
            binding.cardEventDeskripsi.text = event.deskripsi
        }

        override fun onClick(v: View?) {
            listener.onEventCardClickListener(absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val binding = CardEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return EventHolder(binding)
    }

    override fun onBindViewHolder(holder: EventHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size

}