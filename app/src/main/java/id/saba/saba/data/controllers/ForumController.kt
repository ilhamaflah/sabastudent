package id.saba.saba.data.controllers

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.saba.saba.HOST
import id.saba.saba.data.adapters.CommentAdapter
import id.saba.saba.data.adapters.ForumAdapter
import id.saba.saba.data.models.Comment
import id.saba.saba.data.models.Forum
import id.saba.saba.data.models.User
import org.json.JSONException
import org.json.JSONObject
import splitties.toast.toast

class ForumController() {
    fun listForum(
        forums: ArrayList<Forum>,
        adapter: ForumAdapter,
        username: String,
        konteks: Context
    ) {
        val queue = Volley.newRequestQueue(konteks)
        val url = HOST().Host + "api/forum"
        val stringRequest = object : StringRequest(Request.Method.POST, url, {
            try {
                val obj = JSONObject(it)
                val arr = obj.getJSONArray("forum")
                for (i in 0 until arr.length()) {
                    val comments = ArrayList<Comment>()
                    val data = arr.getJSONObject(i)
                    val uploader = data.getJSONObject("uploader")
                    if (data.has("komentar_forum")) {
                        for (x in 0 until data.getJSONArray("komentar_forum").length()) {
                            val data_2 = data.getJSONArray("komentar_forum").getJSONObject(x)
                            comments.add(
                                Comment(
                                    data_2.getInt("id"),
                                    data_2.getString("userKomen"),
                                    data_2.getString("userKomenPic"),
                                    data_2.getString("timestamp"),
                                    data_2.getString("komentar"),
                                    0,
                                    0
                                )
                            )
                        }

                    }
                    forums.add(
                        Forum(
                            data.getInt("id"),
                            data.getString("judul"),
                            data.getString("deskripsi"),
                            data.getString("thumbnail"),
                            data.getString("timestamp"),
                            User(
                                i,
                                uploader.getString("nama"),
                                uploader.getString("photo"),
                                uploader.getString("username")
                            ),
                            data.getInt("total_like"), 100, 101, comments
                        )
                    )
                    //adapter.notifyItemInserted(i)
                }
                when (forums.size) {
                    0 -> adapter.notifyDataSetChanged()
                    1 -> adapter.notifyDataSetChanged()
                    else -> {
                        forums.reverse()
                        adapter.notifyDataSetChanged()
                    }
                }
            } catch (e: JSONException) {
                toast(e.message.toString())
            }
        }, {
            toast(it.message.toString())
        }) {
            override fun getParams(): MutableMap<String, String> {
                return HashMap<String, String>().apply {
                    put("user_username", username)
                }
            }
        }
        queue.add(stringRequest)
    }

    fun addKomentar(
        comments: ArrayList<Comment>,
        adapter: CommentAdapter,
        konteks: Context,
        id: Int,
        username: String,
        komentar: String
    ) {
        val queue = Volley.newRequestQueue(konteks)
        val url = HOST().Host + "api/forum/" + id + "/komentar"
        val stringRequest = object : StringRequest(Request.Method.POST, url, {
            try {
                val obj = JSONObject(it)
                if (obj.getBoolean("success")) {
                    val data = obj.getJSONObject("data")
                    comments.add(
                        Comment(
                            data.getInt("id"), obj.getString("userKomen"), obj.getString("userImg"),
                            data.getString("timestamp"), data.getString("komentar"), 100, 5
                        )
                    )
                    toast("Komentar terkirim")
                } else {
                    toast("Komentar gagal terkirim")
                }
                adapter.notifyDataSetChanged()
            } catch (e: JSONException) {
                toast(e.message.toString())
            }
        }, {
            toast(it.message.toString())
        }) {
            override fun getParams(): MutableMap<String, String> {
                return HashMap<String, String>().apply {
                    put("komentar", komentar)
                    put("user_username", username)
                }
            }
        }
        queue.add(stringRequest)
    }
}