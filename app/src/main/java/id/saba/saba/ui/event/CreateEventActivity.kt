package id.saba.saba.ui.event

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import id.saba.saba.VolleyMultipartRequest
import id.saba.saba.data.controllers.EventController
import id.saba.saba.data.models.Event
import id.saba.saba.data.models.User
import id.saba.saba.databinding.ActivityCreateEventBinding
import splitties.toast.toast
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class CreateEventActivity : AppCompatActivity() {
    private val GALLERY = 100
    private val GALLERY_PERMISSION_CODE = 101

    private lateinit var binding: ActivityCreateEventBinding
    private lateinit var startDatePicker: MaterialDatePicker<Long>
    private lateinit var endDatePicker: MaterialDatePicker<Long>
    private lateinit var startTimePicker: MaterialTimePicker

    // ngambil valuenya: endTimePicker.hour / endTimePicker.hour
    // valuenya 0, 1, 2, 3 dst. jadi perlu ditambah huruf 0 kalo value < 10
    private lateinit var endTimePicker: MaterialTimePicker
    private lateinit var imageUri: Uri
    var bitmap : Bitmap? = null
    private lateinit var sharedPref: SharedPreferences

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
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, result)
                    }

                    override fun onError(e: Exception?) {}
                })
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = this.getSharedPreferences("SABA", Context.MODE_PRIVATE)
        initView()
    }

    private fun initView() {
        binding.btnBack.setOnClickListener { onBackPressed() }

        binding.cardImgEvent.setOnClickListener {
            openGallery()
        }

        binding.txtDateStart.setOnClickListener {
            val constraintsBuilder =
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())

            startDatePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select event start date")
                    .setCalendarConstraints(constraintsBuilder.build())
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            startDatePicker.show(supportFragmentManager, "DATE_START")

            startDatePicker.addOnPositiveButtonClickListener {
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val startDate = formatter.format(Date(it))
                binding.txtDateStart.setText(startDate)
            }
        }

        binding.txtTimeStart.setOnClickListener {
            startTimePicker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setTitleText("Select event start time")
                    .build()

            startTimePicker.show(supportFragmentManager, "TIME_START")

            startTimePicker.addOnPositiveButtonClickListener {
                val hour =
                    if (startTimePicker.hour < 10) "0${startTimePicker.hour}" else startTimePicker.hour
                val minute =
                    if (startTimePicker.minute < 10) "0${startTimePicker.minute}" else startTimePicker.minute
                binding.txtTimeStart.setText("${hour}:${minute}")
            }
        }

        binding.txtDateEnd.setOnClickListener {
            val constraintsBuilder =
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())

            endDatePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select event start date")
                    .setCalendarConstraints(constraintsBuilder.build())
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            endDatePicker.show(supportFragmentManager, "DATE_END")

            endDatePicker.addOnPositiveButtonClickListener {
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val startDate = formatter.format(Date(it))
                binding.txtDateEnd.setText(startDate)
            }
        }

        binding.txtTimeEnd.setOnClickListener {
            endTimePicker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setTitleText("Select event start time")
                    .build()

            endTimePicker.show(supportFragmentManager, "TIME_END")

            endTimePicker.addOnPositiveButtonClickListener {
                val hour =
                    if (endTimePicker.hour < 10) "0${endTimePicker.hour}" else endTimePicker.hour
                val minute =
                    if (endTimePicker.minute < 10) "0${endTimePicker.minute}" else endTimePicker.minute
                binding.txtTimeEnd.setText("${hour}:${minute}")
            }
        }

        binding.btnCreateEvent.setOnClickListener {
            if(bitmap == null){ toast("Foto masih kosong") }
            else{ create() } }
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

    private fun create() {
        /*val params = HashMap<String, String>()
        params["judul"] = binding.txtTitle.text.toString()
        params["deskripsi"] = binding.txtDescription.text.toString()
        params["contact_person"] = binding.txtContact.text.toString()
        params["link_registrasi"] = binding.txtLink.text.toString()
        params["start_date"] = binding.txtDateStart.text.toString()
        params["end_date"] = binding.txtDateEnd.text.toString()
        params["start_time"] = binding.txtTimeStart.text.toString()
        params["end_time"] = binding.txtTimeEnd.text.toString()
        params["thumbnail"] = "aaaa" // replace imageUri jadi base64

        Log.d("EVENT", params.toString())*/

        /*val USER = sharedPref.getString("USER", "")
        val volleyMultipartRequest = object : VolleyMultipartRequest(
            Request.Method.POST, HOST().Host + "api/forum/tambah", Response.Listener<NetworkResponse> {
            fun onResponse(response: NetworkResponse) {
                try {
                    val obj = JSONObject(String(response.data))
                    longToast("Normal: " + obj.getString("success"))
                } catch (e: JSONException) {
                    longToast("Error: " + e.message.toString())
                }
            }
            onResponse(it)
        },
            Response.ErrorListener {
                toast(it.message.toString())
            }){
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
                    put("thumbnail", DataPart("$imagename.png", getFileDataFromDrawable(bitmap)))
                }
            }
        }
        Volley.newRequestQueue(this).add(volleyMultipartRequest)*/
        val USER = sharedPref.getString("USER", "")
        EventController().postEvent(Event(0, "", binding.txtTitle.text.toString(), "",
            User(0, USER.toString(), "https://images.unsplash.com/photo-1499242249421-ac7daa30e504?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80", "username"), binding.txtDescription.text.toString(), "",
            "", binding.txtDateStart.text.toString(), binding.txtDateEnd.text.toString(),
            binding.txtTimeStart.text.toString(), binding.txtTimeStart.text.toString(),
            binding.txtContact.text.toString(), binding.txtLink.text.toString()), bitmap!!, this)
        Handler().postDelayed(this::onBackPressed, 1000)
    }

    fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        //val encodeBase64 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
        return byteArrayOutputStream.toByteArray()
    }
}