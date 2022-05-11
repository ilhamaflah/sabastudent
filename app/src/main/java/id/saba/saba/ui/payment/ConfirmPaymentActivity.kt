package id.saba.saba.ui.payment

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import id.saba.saba.data.models.Payment
import id.saba.saba.databinding.ActivityConfirmPaymentBinding

class ConfirmPaymentActivity : AppCompatActivity() {
    private val GALLERY_PERMISSION_CODE = 101

    private lateinit var binding: ActivityConfirmPaymentBinding
    private lateinit var payment: Payment
    private lateinit var imageUri: Uri

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            if (result != null) {
                imageUri = result
                Picasso.get().load(result).into(binding.imgEvent, object : Callback {
                    override fun onSuccess() {
                        val cardLayoutParams = binding.cardImgEvent.layoutParams
                        cardLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

                        val imgLayoutParams = binding.imgEvent.layoutParams
                        imgLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT

                        binding.cardImgEvent.layoutParams = cardLayoutParams
                        binding.imgEvent.layoutParams = imgLayoutParams
                    }

                    override fun onError(e: Exception?) {}
                })
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConfirmPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        payment = intent.getParcelableExtra("PAYMENT")!!

        initView()
    }

    private fun initView() {
        binding.btnBack.setOnClickListener { onBackPressed() }

        binding.cardImgEvent.setOnClickListener {
            openGallery()
        }

        binding.btnConfirm.setOnClickListener { upload() }
    }

    private fun openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //permission denied
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permission, GALLERY_PERMISSION_CODE)
                resultLauncher.launch("image/*")
            } else {
                //permission already granted
                resultLauncher.launch("image/*")
            }
        } else {
            //system os is < Marshmallow
            resultLauncher.launch("image/*")
        }
    }

    private fun upload() {
        // ambil id payment dari data payment yang dikirim dari intent (line 20)
        val base64 = "aa" // ubah imageUri jadi base64, trus post ke server
    }
}