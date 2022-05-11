package id.saba.saba.ui.form

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import id.saba.saba.HOST
import id.saba.saba.VolleyMultipartRequest
import id.saba.saba.databinding.ActivityFormBinding
import org.json.JSONException
import org.json.JSONObject
import splitties.toast.longToast
import splitties.toast.toast
import java.io.ByteArrayOutputStream


class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    private lateinit var tipe: String
    private lateinit var backDialog: AlertDialog
    private lateinit var img: Uri
    var bitmap: Bitmap? = null
    private lateinit var sharedPref: SharedPreferences

    private val launcher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        img = it
        Picasso.get().load(it).into(binding.imgForm)
        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = this.getSharedPreferences("SABA", Context.MODE_PRIVATE)
        loadData()
        initView()
    }

    private fun loadData() {
        tipe = intent.getStringExtra("FORM") ?: ""
    }

    private fun initView() {
        val dialog = AlertDialog.Builder(this)
        dialog.apply {
            setTitle("Hapus perubahan?")
            setMessage("Perubahan form anda akan hilang")
            setPositiveButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            setNegativeButton("Hapus") { _, _ -> finish() }
        }
        backDialog = dialog.create()

        binding.imgForm.setOnClickListener {
            openGallery()
        }

        binding.btnFormPost.setOnClickListener {
            Log.d("FORM", img.toString())
            if (bitmap == null) {
            } else {
                uploadBitmap(bitmap!!)
            }

        }

        binding.btnBack.setOnClickListener { onBackPressed() }
    }

    private fun openGallery() {
        launcher.launch("image/*")
    }

    fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        //val encodeBase64 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
        return byteArrayOutputStream.toByteArray()
    }

    override fun onBackPressed() {
        if (binding.txtFormJudul.text.toString().isNotEmpty() || binding.txtFormIsi.text.toString()
                .isNotBlank()
        ) {
            backDialog.show()
        } else {
            finish()
        }
    }

    private fun uploadBitmap(bitmap: Bitmap) {
        val USER = sharedPref.getString("USER", "")
        val volleyMultipartRequest = object : VolleyMultipartRequest(
            Request.Method.POST,
            HOST().Host + "api/forum/tambah",
            Response.Listener<NetworkResponse> {
                fun onResponse(response: NetworkResponse) {
                    try {
                        val obj = JSONObject(String(response.data))
                        longToast("Normal: " + obj.getString("success"))
                        onBackPressed()
                    } catch (e: JSONException) {
                        longToast("Error: " + e.message.toString())
                    }
                }
                onResponse(it)
            },
            Response.ErrorListener {
                toast(it.message.toString())
            }) {
            override fun getParams(): Map<String, String> {
                return HashMap<String, String>().apply {
                    put("judul", binding.txtFormJudulLayout.editText?.text.toString())
                    put("deskripsi", binding.txtLayoutFormIsi.editText?.text.toString())
                    put("user_username", USER.toString())
                }
            }

            override fun getByteData(): MutableMap<String, DataPart> {
                val imagename = System.currentTimeMillis()
                return HashMap<String, DataPart>().apply {
                    getFileDataFromDrawable(bitmap)?.let { bm -> DataPart("$imagename.png", bm) }
                        ?.let {
                            put(
                                "thumbnail",
                                it
                            )
                        }
                }
            }
        }
        Volley.newRequestQueue(this).add(volleyMultipartRequest)
    }
}