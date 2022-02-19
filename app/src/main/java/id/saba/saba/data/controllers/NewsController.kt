package id.saba.saba.data.controllers

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.saba.saba.HOST
import id.saba.saba.data.adapters.NewsAdapter
import id.saba.saba.data.models.News
import id.saba.saba.data.models.User
import org.json.JSONException
import org.json.JSONObject
import splitties.toast.toast

class NewsController() {
    fun listNews(news: ArrayList<News>, adapter: NewsAdapter, konteks: Context){
        val queue = Volley.newRequestQueue(konteks)
        val url = HOST().Host + "api/berita"
        val stringRequest = StringRequest(Request.Method.GET, url, {
                try {
                    //val arr = JSONArray(it)
                    val obj = JSONObject(it)
                    val arr = obj.getJSONArray("berita")
                    for (i in 0 until arr.length()) {
                        val data = arr.getJSONObject(i)
                        val uploader = data.getJSONObject("uploader")
                        news.add(
                            News(
                                data.getInt("id"),
                                User(i, uploader.getString("nama"), uploader.getString("photo"), uploader.getString("username")),
                                data.getString("judul"), data.getString("thumbnail"),
                                data.getString("timestamp"), data.getString("kutipan"), data.getString("deskripsi"),
                                false))
                        //adapter.notifyItemInserted(i)
                    }
                    when (news.size) {
                        0 -> adapter.notifyDataSetChanged()
                        1 -> adapter.notifyDataSetChanged()
                        else -> {
                            news.reverse()
                            adapter.notifyDataSetChanged()
                        }
                    }
                } catch (e: JSONException) {
                    toast(e.message.toString())
                }
            }, {
                toast(it.message.toString())
            })
        queue.add(stringRequest)
    }
}