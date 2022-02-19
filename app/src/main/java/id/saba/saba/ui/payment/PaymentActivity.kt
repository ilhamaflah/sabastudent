package id.saba.saba.ui.payment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import id.saba.saba.data.adapters.PaymentAdapter
import id.saba.saba.data.models.Payment
import id.saba.saba.databinding.ActivityPaymentBinding
import java.util.*

class PaymentActivity : AppCompatActivity(), PaymentAdapter.OnPaymentClickListener {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var payments: ArrayList<Payment>
    private lateinit var adapter: PaymentAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        loadData()
    }

    private fun initView() {
        payments = arrayListOf()
        adapter = PaymentAdapter(payments, this)
        layoutManager = LinearLayoutManager(this)

        binding.paymentRV.adapter = adapter
        binding.paymentRV.layoutManager = layoutManager

        binding.btnBack.setOnClickListener { onBackPressed() }
    }

    private fun loadData() {
        // api get payment list
        for (i in 1 until 10) {
            payments.add(
                Payment(
                    i,
                    "Payment ${i}",
                    "Deskripsi ${i}",
                    (30000..120000).random(),
                    Date().toString(),
                    Date().toString(),
                    (0..1).random(),
                    "User",
                    ""
                )
            )
            adapter.notifyItemInserted(i - 1)
        }
    }

    override fun onPaymentCardClickListener(position: Int) {
//        start<ConfirmPaymentActivity> {
//            putExtra("PAYMENT", payments[position])
//        }
        val detailPaymentSheet = DetailPaymentFragment.newInstance(payments[position])
        detailPaymentSheet.show(supportFragmentManager, DetailPaymentFragment.TAG)
    }
}