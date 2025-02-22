package ochat.wearendar.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDate.formatDate(): String {
    return this.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault()))
}

fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}