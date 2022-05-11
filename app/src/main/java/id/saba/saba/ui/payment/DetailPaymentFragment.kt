package id.saba.saba.ui.payment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.saba.saba.R
import id.saba.saba.data.models.Payment
import id.saba.saba.databinding.FragmentDetailPaymentBinding
import splitties.fragments.start
import splitties.intents.start

private const val ARG_PAYMENT = "PAYMENT"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailPaymentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailPaymentFragment : BottomSheetDialogFragment() {
    // TODO: Rename and change types of parameters
    private var _payment: Payment? = null
    private var _binding: FragmentDetailPaymentBinding? = null

    private val binding get() = _binding!!
    private val payment get() = _payment!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            _payment = it.getParcelable(ARG_PAYMENT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailPaymentBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        Log.d("PAYMENT", payment.toString())
        if (payment.konfirmasi == 0) {
            binding.btnConfirm.visibility = View.VISIBLE
        }

        binding.btnBack.setOnClickListener { dismiss() }

        binding.btnConfirm.setOnClickListener {
            start<ConfirmPaymentActivity> {
                putExtra("PAYMENT", payment)
            }
        }

        when (payment.konfirmasi) {
            0 -> {
                binding.txtStatus.text = "Payment pending"
                binding.txtStatus.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.amber_500
                    )
                )
            }
            1 -> {
                binding.txtStatus.text = "Paid"
                binding.txtStatus.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primary
                    )
                )
            }
        }

        binding.txtID.text = payment.id.toString()
        binding.txtDate.text = payment.tglTransaksi
        binding.txtConfirmDate.text =
            if (payment.tglPembayaran != null) payment.tglPembayaran else "-"
        binding.txtTotal.text = payment.totalString()
        binding.txtTitle.text = payment.judul
        binding.txtDescription.text = payment.deskripsi
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param payment Parameter 1.
         * @return A new instance of fragment DetailPaymentFragment.
         */
        @JvmStatic
        fun newInstance(payment: Payment) =
            DetailPaymentFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PAYMENT, payment)
                }
            }

        const val TAG = "payment_detail"
    }
}