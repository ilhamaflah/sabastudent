package id.saba.saba.data.controllers

import android.content.Context
import android.graphics.Bitmap
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.saba.saba.HOST
import id.saba.saba.VolleyMultipartRequest
import id.saba.saba.data.adapters.EventAdapter
import id.saba.saba.data.models.Event
import id.saba.saba.data.models.User
import org.json.JSONException
import org.json.JSONObject
import splitties.toast.longToast
import splitties.toast.toast
import java.io.ByteArrayOutputStream

class EventController() {
    fun postEvent(model: Event, bitmap: Bitmap, konteks: Context) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        //val encodeBase64 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)

        val volleyMultipartRequest = object : VolleyMultipartRequest(
            Method.POST,
            HOST().Host + "api/event/tambah",
            Response.Listener<NetworkResponse> {
                fun onResponse(response: NetworkResponse) {
                    try {
                        val obj = JSONObject(String(response.data))
                        toast("Normal: " + obj.getString("success"))
                        if (obj.has("message")) {
                            toast(obj.getString("message"))
                        }
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
                    put("judul", model.judul)
                    put("deskripsi", model.deskripsi)
                    put("start_date", model.tanggalMulai)
                    put("start_time", model.waktuMulai)
                    put("end_date", model.tanggalAkhir)
                    put("end_time", model.waktuAkhir)
                    put("contact_person", model.contact_person)
                    put("link_registrasi", model.link_registrasi)
                    put("user_username", model.user.username)
                }
            }

            override fun getByteData(): MutableMap<String, DataPart> {
                val imagename = System.currentTimeMillis()
                return HashMap<String, DataPart>().apply {
                    put(
                        "thumbnail",
                        DataPart("$imagename.png", byteArrayOutputStream.toByteArray())
                    )
                }
            }
        }
        Volley.newRequestQueue(konteks).add(volleyMultipartRequest)
    }

    fun listEvent(events: ArrayList<Event>, adapter: EventAdapter, konteks: Context) {
        val queue = Volley.newRequestQueue(konteks)
        val url = HOST().Host + "api/event"
        val stringRequest = StringRequest(Request.Method.GET, url, {
            try {
                val obj = JSONObject(it)
                val arr = obj.getJSONArray("event")
                for (i in 0 until arr.length()) {
                    val data = arr.getJSONObject(i)
                    val uploader = data.getJSONObject("uploader")
                    events.add(
                        Event(
                            data.getInt("id"),
                            data.getString("thumbnail"),
                            data.getString("judul"), data.getString("kutipan"),
                            User(
                                i,
                                uploader.getString("nama"),
                                uploader.getString("photo"),
                                uploader.getString("username")
                            ),
                            data.getString("deskripsi"),
                            "",
                            data.getString("timestamp"),
                            data.getString("start"),
                            data.getString("end"),
                            data.getString("start"),
                            data.getString("end"),
                            data.getString("contact_person"),
                            data.getString("link_registrasi")
                        )
                    )
                    //adapter.notifyItemInserted(i)
                }
                events.reverse()
                adapter.notifyDataSetChanged()
            } catch (e: JSONException) {
                toast(e.message.toString())
            }
        }, {
            toast(it.message.toString())
        })
        queue.add(stringRequest)
    }
}