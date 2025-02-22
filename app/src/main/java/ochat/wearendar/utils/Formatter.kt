package ochat.wearendar.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDate.formatDate(): String {
    return this.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault()))
}