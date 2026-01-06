//package com.prayatna.lookiesapp.utils
//
//import java.time.LocalDate
//import java.time.format.DateTimeFormatter
//import java.util.Locale
//
//object DateHelper {
//    fun formatDate(dateString: String): String {
//        return try {
//            // Asumsi format backend: "2023-10-25" atau "2023-10-25T10:00:00Z"
//            // Kita ambil bagian tanggalnya saja
//            val datePart = dateString.split("T")[0]
//            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
//            val date = LocalDate.parse(datePart, inputFormatter)
//
//            // Output: "25 Oct 2023"
//            val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
//            date.format(outputFormatter)
//        } catch (e: Exception) {
//            dateString // Jika gagal parsing, kembalikan string aslinya
//        }
//    }
//}