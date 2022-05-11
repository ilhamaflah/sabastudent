package id.saba.saba.ui.payment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import id.saba.saba.HOST
import id.saba.saba.VolleyMultipartRequest
import id.saba.saba.data.models.Payment
import id.saba.saba.databinding.ActivityConfirmPaymentBinding
import org.json.JSONObject
import java.io.ByteArrayOutputStream


class ConfirmPaymentActivity : AppCompatActivity() {
    private val GALLERY_PERMISSION_CODE = 101

    private lateinit var binding: ActivityConfirmPaymentBinding
    private lateinit var payment: Payment
    private lateinit var imageUri: Uri
    private lateinit var imageBase64: String

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
        val sharedPref = this.getSharedPreferences("SABA", Context.MODE_PRIVATE)
        val USER = sharedPref.getString("USER", "")
        val queue = Volley.newRequestQueue(this)
        val url = "${HOST().Host}api/$USER/history-pembayaran/${payment.id}/bukti"
        val request = object : VolleyMultipartRequest(
            Method.POST,
            url,
            { res ->
                try {
                    Log.d("res", String(res.data))
                    val data = JSONObject(String(res.data))

                    if (data.getBoolean("success")) {
                        finish()
                        Snackbar
                            .make(
                                binding.layout,
                                "Success confirming payment",
                                Snackbar.LENGTH_SHORT
                            )
                            .show()
                    } else {
                        Log.d("RES", res.toString())
                        Snackbar
                            .make(
                                binding.layout,
                                "An error occurred, try again later",
                                Snackbar.LENGTH_SHORT
                            )
                            .show()
                    }
                } catch (e: Exception) {
                    Log.e("ERR_RES", e.message.toString())
                    Snackbar
                        .make(
                            binding.layout,
                            "An error occurred, try again later",
                            Snackbar.LENGTH_SHORT
                        )
                        .show()
                }
            },
            { err ->
                Log.e("ERR", err.networkResponse.toString())
                Snackbar
                    .make(
                        binding.layout,
                        "An error occurred, try again later",
                        Snackbar.LENGTH_SHORT
                    )
                    .show()
            }
        ) {
            override fun getByteData(): Map<String, DataPart>? {
                val imagename = System.currentTimeMillis()
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
                val byteArray: ByteArray = outputStream.toByteArray()

                return HashMap<String, DataPart>().apply {
                    put(
                        "thumbnail",
                        DataPart("$imagename.png", byteArray)
                    )
                }
            }
        }

        queue.add(request)
    }
}