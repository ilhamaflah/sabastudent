package id.saba.saba.data.controllers

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.saba.saba.HOST
import id.saba.saba.data.adapters.ScholarshipAdapter
import id.saba.saba.data.models.Company
import id.saba.saba.data.models.Scholarship
import org.json.JSONException
import org.json.JSONObject
import splitties.toast.toast

class ScholarshipsController() {
    fun listScholarship(scholarships: ArrayList<Scholarship>, adapter: ScholarshipAdapter, konteks: Context){
        val queue = Volley.newRequestQueue(konteks)
        val url = HOST().Host + "api/beasiswa"
        val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener {
            try {
                val obj = JSONObject(it)
                val arr = obj.getJSONArray("beasiswa")
                for (i in 0 until arr.length()) {
                    val data = arr.getJSONObject(i)
                    scholarships.add(
                        Scholarship(
                            data.getInt("id"),
                            Company(1, "Company", "https://ui-avatars.com/api/?name=Jaya+Abadi", "Surabaya", "000000"),
                            data.getString("thumbnail"),
                            data.getString("judul"),
                            data.getString("kutipan"),
                            data.getString("deskripsi")
                        )
                    )
                }
                scholarships.reverse()
                adapter.notifyDataSetChanged()
            } catch (e: JSONException) {
                toast(e.message.toString())
            }
        }, Response.ErrorListener {
            toast(it.message.toString())
        })
        queue.add(stringRequest)
    }
}