package id.saba.saba.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Company(val id: Int, val nama: String, val photo: String, val lokasi: String, val contact: String) : Parcelable
