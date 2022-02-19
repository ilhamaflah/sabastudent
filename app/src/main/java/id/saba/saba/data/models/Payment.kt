package id.saba.saba.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

@Parcelize
data class Payment(
    val id: Int,
    val judul: String,
    val deskripsi: String,
    val biaya: Int,
    @SerializedName("tgl_transaksi")
    val tglTransaksi: String,
    @SerializedName("tgl_pembayaran")
    var tglPembayaran: String?,
    val konfirmasi: Int,
    @SerializedName("user_username")
    val user: String,
    @SerializedName("foto_verifikasi")
    val fotoVerifikasi: String,
) : Parcelable {
    fun totalString(): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        formatter.maximumFractionDigits = 0

        return formatter.format(biaya)
    }
}
