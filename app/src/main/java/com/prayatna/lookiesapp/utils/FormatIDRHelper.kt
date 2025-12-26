package com.prayatna.lookiesapp.utils

import java.text.NumberFormat
import java.util.Locale

fun formatRupiah(amount: Double): String {
    val localeID = Locale.forLanguageTag("id-ID")
    val format = NumberFormat.getCurrencyInstance(localeID)
    return format.format(amount).replace(",00", "")
}
