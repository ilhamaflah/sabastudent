package id.saba.saba.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Comment(
    val id: Int,
    val user: String,
    val userImg: String,
    val tanggal: String,
    val comment: String,
    var upvote: Int,
    var downvote: Int
) : Parcelable {
    fun postedTimeString(): String {
        val prettyTime = PrettyTime()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = sdf.parse(tanggal)

        return prettyTime.format(date)
    }
}
