package id.saba.saba.ui.tabs.home.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import id.saba.saba.SliderModal
import id.saba.saba.databinding.FragmentHomeSliderBinding

private const val ARG_SLIDER = "slider"

class HomeSliderFragment : Fragment() {
    private lateinit var binding: FragmentHomeSliderBinding
    private var slider: SliderModal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            slider = it.getParcelable(ARG_SLIDER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeSliderBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("SLIDER", slider.toString())
        if (slider != null) {
            Picasso.get().load(slider!!.img).into(binding.idIV)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param slider Slider Modal.
         * @return A new instance of fragment HomeSlider.
         */
        @JvmStatic
        fun newInstance(slider: SliderModal) =
            HomeSliderFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_SLIDER, slider)
                }
            }
    }
}