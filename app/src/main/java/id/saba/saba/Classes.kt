package id.saba.saba

import id.saba.saba.data.models.User

data class SliderModal(val id: Int,
                       val img: String,
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
                       val link_registrasi: String){
}
data class ClassEvent(val name: String, val number: String, val photo: Int) {
}
data class ClassNews(val username: String, val judul: String, val gambar: String, val tanggal: String, val berita: String = "") {
}
data class ClassTrendingForum(val name: String, val tanggal: String, val deskripsi: String) {
}
data class ClassNotifikasi(val id: String, val keterangan: String, val tanggal: String, val photo: Int) {
}