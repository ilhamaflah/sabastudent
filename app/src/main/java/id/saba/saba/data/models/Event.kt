package id.saba.saba.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Event(
    val id: Int,
    val gambar: String,
    val judul: String,
    val headline: String,
    val user: User,
    val deskripsi: String,
    val lokasi: String,
    val tanggal: String,
    val tanggalMulai: String,
    val tanggalAkhir: String,
    val waktuMulai: String,
    val waktuAkhir: String,
    val contact_person: String,
    val link_registrasi: String
) : Parcelable {
    fun textTanggal(): String {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        calendar.time = sdf.parse(tanggalMulai)!!
        val mulai = calendar.get(Calendar.DAY_OF_MONTH)
        val bulan_m = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())

        calendar.time = sdf.parse(tanggalAkhir)!!
        val akhir = calendar.get(Calendar.DAY_OF_MONTH)
        val bulan_a = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())

        return "$mulai $bulan_m - $akhir $bulan_a"
    }

    fun textWaktu(): String {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        calendar.time = sdf.parse(waktuMulai)!!
        val waktu_m = calendar.get(Calendar.HOUR_OF_DAY)
        val menit_m = calendar.get(Calendar.MINUTE)
        val mulai: String = if (waktu_m > 12) {
            "${waktu_m - 12}:$menit_m pm"
        } else {
            "$waktu_m:$menit_m am"
        }

        calendar.time = sdf.parse(waktuAkhir)!!
        val waktu_a = calendar.get(Calendar.HOUR_OF_DAY)
        val menit_a = calendar.get(Calendar.MINUTE)
        val akhir = if (waktu_a > 12) {
            "${waktu_a - 12}:$menit_a pm"
        } else {
            "$waktu_a:$menit_a am"
        }

        return "Opentime: $mulai - $akhir"
    }
}
