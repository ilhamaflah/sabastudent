package id.saba.saba.ui.payment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import id.saba.saba.HOST
import id.saba.saba.databinding.ActivityCreatePaymentBinding
import org.json.JSONObject

class CreatePaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePaymentBinding
    private val nominals = arrayListOf(20000, 40000, 60000, 80000, 100000)
    private lateinit var sharedPref: SharedPreferences

    private var selectedNominal = 20000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreatePaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            nominals
        )
        binding.txtNominal.setAdapter(adapter)
        binding.txtNominal.setSelection(0)
        binding.txtNominal.setText(nominals[0].toString(), false)

        binding.txtNominal.setOnItemClickListener { parent, view, position, id ->
            selectedNominal = nominals[position]
        }

        binding.btnBack.setOnClickListener { onBackPressed() }

        binding.btnCreatePayment.setOnClickListener {
            topUp()
        }
    }

    private fun topUp() {
        sharedPref = this.getSharedPreferences("SABA", Context.MODE_PRIVATE)
        val USER = sharedPref.getString("USER", "")
        val title = binding.txtTitle.text.toString()
        val description = binding.txtDescription.text.toString()
        val nominal = selectedNominal

        val params: MutableMap<String, String> = HashMap()
        params["judul"] = title
        params["deskripsi"] = description
        params["biaya"] = nominal.toString()

        val queue = Volley.newRequestQueue(this)
        val url = "${HOST().Host}api/$USER/history-pembayaran/tambah"
        val request = object : StringRequest(
            Method.POST,
            url,
            { res ->
                try {
                    val data = JSONObject(res)

                    if (data.getBoolean("success")) {
                        finish()
                        Snackbar
                            .make(
                                binding.root.rootView,
                                "Success adding payment",
                                Snackbar.LENGTH_SHORT
                            )
                            .show()
                    } else {
                        Log.d("RES", res.toString())
                        Snackbar
                            .make(
                                binding.root.rootView,
                                "An error occurred, try again later",
                                Snackbar.LENGTH_SHORT
                            )
                            .show()
                    }
                } catch (e: Exception) {
                    Log.e("ERR_RES", e.message.toString())
                    Snackbar
                        .make(
                            binding.root.rootView,
                            "An error occurred, try again later",
                            Snackbar.LENGTH_SHORT
                        )
                        .show()
                }
            },
            { err ->
                Log.e("ERR", err.message.toString())
                Snackbar
                    .make(
                        binding.root.rootView,
                        "An error occurred, try again later",
                        Snackbar.LENGTH_SHORT
                    )
                    .show()
            }
        ) {
            override fun getParams() = params
        }

        queue.add(request)
    }
}