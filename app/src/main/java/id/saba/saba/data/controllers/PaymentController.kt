package id.saba.saba.data.controllers

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.saba.saba.HOST
import id.saba.saba.data.models.Payment
import org.json.JSONException
import org.json.JSONObject
import splitties.toast.longToast
import splitties.toast.toast

class PaymentController {
    fun addPayment(model: Payment, konteks: Context, judul: String, deskripsi: String, biaya: String, username: String){
        val queue = Volley.newRequestQueue(konteks)
        val url = HOST().Host + "api/" + username + "/history-pembayaran/tambah"
        val stringRequest = object : StringRequest(Request.Method.POST, url, {
            try {
                val obj = JSONObject(it)
                if(obj.getBoolean("success")){
                    val data = obj.getJSONObject("data")
                    longToast("Silahkan lengkapi bukti top up di Top Up History")
                }
                else{ toast("Pembayaran gagal diproses") }
            } catch (e: JSONException) {
                toast(e.message.toString())
            }
        }, {
            toast(it.message.toString())
        }){
            override fun getParams(): MutableMap<String, String> {
                return HashMap<String, String>().apply {
                    put("judul", judul)
                    put("deskripsi", deskripsi)
                    put("biaya", biaya)
                }
            }
        }
        queue.add(stringRequest)
    }
}