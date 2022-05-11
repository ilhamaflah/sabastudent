package id.saba.saba.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.smarteist.autoimageslider.SliderViewAdapter
import android.widget.Toast
import com.squareup.picasso.Picasso
import id.saba.saba.R
import id.saba.saba.SliderModal
import kotlinx.android.synthetic.main.slider_layout.view.*

class SliderAdapter(private val context: Context, sliderModalArrayList: ArrayList<SliderModal>) : SliderViewAdapter<SliderAdapter.SliderAdapterVH>() {

    private var data = sliderModalArrayList

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate: View =
            LayoutInflater.from(parent.context).inflate(R.layout.slider_layout, parent, false)

        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        val sliderItem = data[position]
        //viewHolder.imgBackground.setBackgroundResource(sliderItem.img)
        /*if(data[position].title == "Kost"){
            viewHolder.imgBackground.scaleType = ImageView.ScaleType.CENTER_CROP
        }*/
        Picasso.get().load(sliderItem.img).into(viewHolder.imgBackground)
        viewHolder.itemView.setOnClickListener {
            Toast.makeText(context, "This is item in position $position", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getCount() = data.size

    class SliderAdapterVH(itemView: View): ViewHolder(itemView) {
        val imgBackground: ImageView = itemView.idIV
    }
}