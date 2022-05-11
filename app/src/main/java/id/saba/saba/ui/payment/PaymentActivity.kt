package id.saba.saba.ui.payment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import id.saba.saba.HOST
import id.saba.saba.data.adapters.PaymentAdapter
import id.saba.saba.data.models.Payment
import id.saba.saba.databinding.ActivityPaymentBinding
import org.json.JSONObject
import splitties.toast.toast

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
        val prefs = getSharedPreferences("SABA", Context.MODE_PRIVATE)
        val user = prefs.getString("USER", "")
        val queue = Volley.newRequestQueue(this)
        val url = HOST().Host + "api/${user}/history-pembayaran/"
        val stringRequest = StringRequest(
            url,
            { res ->
                try {
                    val data = JSONObject(res)
                    val objPayments = data.getString("pembayaran")
                    val newPayments: ArrayList<Payment> = Gson().fromJson(objPayments, Payment.listTypeToken)
                    payments.clear()
                    payments.addAll(newPayments)
                    adapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    toast(e.message.toString())
                }
            },
            { err ->
                toast(err.message.toString())
            }
        )

        queue.add(stringRequest)
    }

    override fun onPaymentCardClickListener(position: Int) {
//        start<ConfirmPaymentActivity> {
//            putExtra("PAYMENT", payments[position])
//        }
        val detailPaymentSheet = DetailPaymentFragment.newInstance(payments[position])
        detailPaymentSheet.show(supportFragmentManager, DetailPaymentFragment.TAG)
    }
}