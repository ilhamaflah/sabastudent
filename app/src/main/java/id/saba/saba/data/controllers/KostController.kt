package id.saba.saba.data.controllers

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.model.LatLng
import id.saba.saba.HOST
import id.saba.saba.data.adapters.KostAdapter
import id.saba.saba.data.models.Kost
import org.json.JSONException
import org.json.JSONObject
import splitties.toast.toast

class KostController() {
    fun listKost(kosts: ArrayList<Kost>, adapter: KostAdapter, konteks: Context){
        val queue = Volley.newRequestQueue(konteks)
        val url = HOST().Host + "api/kost"
        val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener {
            try {
                val obj = JSONObject(it)
                val arr = obj.getJSONArray("kost")
                for (i in 0 until arr.length()) {
                    val data = arr.getJSONObject(i)
                    val data_2 = data.getJSONArray("gambar_kost")
                    val data_gambar = data_2.getJSONObject(0)
                    kosts.add(
                        Kost(
                            data.getInt("id"),
                            data_gambar.getString("photo"),
                            data.getString("nama"), 4.5,
                            "Kost Umum",
                            data.getString("alamat"),
                            LatLng(data.getDouble("latitude"), data.getDouble("longitude")),
                            data.getString("contact_person"),
                            false
                        )
                    )
                    adapter.notifyItemInserted(i)
                }
            } catch (e: JSONException) {
                toast(e.message.toString())
            }
        }, Response.ErrorListener {
            toast(it.message.toString())
        })
        queue.add(stringRequest)
    }
}