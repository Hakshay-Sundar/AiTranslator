package com.numad.aitranslator.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Calendar

fun formatTimestamp(timestampMillis: Long): String {
    val date = Date(timestampMillis)
    val calendar = Calendar.getInstance()

    val currentYear = calendar.get(Calendar.YEAR)

    calendar.time = date
    val timestampYear = calendar.get(Calendar.YEAR)

    val format = if (timestampYear == currentYear) {
        SimpleDateFormat("hh:mm a MMM dd", Locale.getDefault())
    } else {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    }

    return format.format(date)
}