package id.saba.saba.data.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.saba.saba.R
import id.saba.saba.data.models.Payment
import id.saba.saba.databinding.CardPaymentBinding

class PaymentAdapter(val data: ArrayList<Payment>, private val listener: OnPaymentClickListener): RecyclerView.Adapter<PaymentAdapter.PaymentHolder>() {

    interface OnPaymentClickListener {
        fun onPaymentCardClickListener(position: Int)
    }

    inner class PaymentHolder(private val binding: CardPaymentBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.cardPayment.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onPaymentCardClickListener(absoluteAdapterPosition)
        }

        fun bind(payment: Payment) {
            binding.txtCardPaymentTitle.text = payment.judul
            binding.txtCardPaymentDescription.text = payment.deskripsi
            binding.txtCardPaymentCost.text = payment.totalString()

            when (payment.konfirmasi) {
                0 -> {
                    binding.txtCardPaymentStatus.text = "Payment pending"
                    binding.txtCardPaymentStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.amber_500))
                }
                1 -> {
                    binding.txtCardPaymentStatus.text = "Paid"
                    binding.txtCardPaymentStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.primary))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentHolder {
        val binding = CardPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PaymentHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size

}