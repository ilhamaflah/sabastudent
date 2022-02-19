package id.saba.saba.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Internship(
    val id: Int,
    val thumbnail: String,
    val judul: String,
    val kutipan: String,
    val company: Company,
    val tanggal: String,
    val content: String
) : Parcelable {
    fun html(): String {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <meta charset=\"utf-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "        <style type=\"text/css\">\n" +
                "           img {\n" +
                "           max-width: 100%;\n" +
                "           }\n" +
                "            @font-face {\n" +
                "                font-family: Montserrat;\n" +
                "                src: url(\"file:///android_res/www/fonts/montserrat_regular.ttf\")\n" +
                "            }\n" +
                "            body {\n" +
                "                font-family: 'Montserrat', sans-serif;\n" +
                "                font-size: 0.9rem" +
                "            }\n" +
                "        </style>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "       <img src='$thumbnail'/>\n" +
                "       $content" +
                "    </body>\n" +
                "</html>"
    }
}
