package id.saba.saba.data.models

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Kost(
    val id: Int,
    val gambar: String,
    val nama: String,
    val rating: Double,
    val kategori: String,
    val lokasi: String,
    val koordinat: LatLng,
    val kontak: String,
    var boomarked: Boolean
) : Parcelable
