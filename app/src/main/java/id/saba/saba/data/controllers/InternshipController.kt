package id.saba.saba.data.controllers

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.saba.saba.HOST
import id.saba.saba.data.adapters.InternshipAdapter
import id.saba.saba.data.models.Company
import id.saba.saba.data.models.Internship
import org.json.JSONException
import org.json.JSONObject
import splitties.toast.toast

class InternshipController() {
    fun listInternship(internships: ArrayList<Internship>, adapter: InternshipAdapter, konteks: Context){
        val queue = Volley.newRequestQueue(konteks)
        val url = HOST().Host + "api/magang"
        val stringRequest = StringRequest(Request.Method.GET, url, {
            try {
                //val arr = JSONArray(it)
                val obj = JSONObject(it)
                val arr = obj.getJSONArray("magang")
                for (i in 0 until arr.length()) {
                    val data = arr.getJSONObject(i)
                    internships.add(
                        Internship(
                            data.getInt("id"),
                            data.getString("thumbnail"),
                            data.getString("judul"), data.getString("kutipan"),
                            Company(1, "Company", "https://ui-avatars.com/api/?name=Jaya+Abadi", "Surabaya", "0000"),
                            data.getString("timestamp"),
                            data.getString("deskripsi")
                        )
                    )
                    //adapter.notifyItemInserted(i)
                }
                internships.reverse()
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