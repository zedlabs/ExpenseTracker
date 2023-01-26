package ml.zedlabs.tbd.util

import android.app.Activity
import android.content.Context
import android.icu.text.CompactDecimalFormat
import android.os.Build
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.WindowInsetsControllerCompat
import ml.zedlabs.tbd.MainApplication
import java.util.Locale
import kotlin.math.roundToInt

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Double.asRating(): String {
    return "${(this * 10.0).roundToInt() / 10.0}/ 10"
}

fun Int.asCompact(locale: Locale): String? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val compactDecimalFormat: CompactDecimalFormat =
            CompactDecimalFormat.getInstance(locale, CompactDecimalFormat.CompactStyle.SHORT)
        compactDecimalFormat.format(this)
    } else {
        this.toString()
    }

}

fun Context?.asApplication(): MainApplication? {
    return (this?.applicationContext as? MainApplication?)
}

fun Activity.changeStatusBarColor(color: Int, isLight: Boolean) {
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = color
    WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = isLight
}