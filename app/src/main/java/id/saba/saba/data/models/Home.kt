package id.saba.saba.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Home(
    val id: Int,
    val user: User,
    val judul: String,
    val kutipan: String,
    var bookmarked: Boolean
) : Parcelable
