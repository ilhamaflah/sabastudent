package id.saba.saba.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Forum(
    val id: Int,
    val headline: String,
    val deskripsi: String,
    val thumbnail: String,
    val tanggal: String,
    val user: User,
    val upvote: Int,
    val downvote: Int,
    val viewer: Int,
    val comments: ArrayList<Comment>
) : Parcelable {
    fun userUsername() = user.username

    fun commentCount() = comments.size

    fun postedTimeString(): String {
        val prettyTime = PrettyTime()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = sdf.parse(tanggal)

        return  prettyTime.format(date)
    }
}
