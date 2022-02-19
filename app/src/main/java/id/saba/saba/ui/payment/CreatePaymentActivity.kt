package id.saba.saba.ui.payment

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import id.saba.saba.databinding.ActivityCreatePaymentBinding

class CreatePaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePaymentBinding
    private val nominals = arrayListOf(30000, 60000, 90000, 120000)

    private var selectedNominal = 30000

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
        val title = binding.txtTitle.text.toString()
        val description = binding.txtDescription.text.toString()
        val nominal = selectedNominal

        val params = HashMap<String, Any>()
        params["judul"] = title
        params["deskripsi"] = description
        params["biaya"] = nominal

        // post params ke server
    }
}