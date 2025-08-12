package com.example.jukang.helper.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun parseFormatWaltu(waktu: String): String{
    val waktuUTC = Instant.parse(waktu)

    val zonawaktuId = ZoneId.of("Asia/Jakarta")
    val waktuIndonesia = waktuUTC.atZone(zonawaktuId)

    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm", Locale("in", "ID"))
    val waltuFormat =waktuIndonesia.format(formatter)
    return waltuFormat

}