package ml.zedlabs.tbd.model.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember

data class CurrencyItem(
    val currencySymbol: String,
    val flag: String,
    val countryName: String,
    val countryCode: String,
    val currency: Currency,
)

data class Currency(
    val currencyCode: String,
    val countryCode: String
) {
    companion object {
        val DEFAULT = Currency("USD", "US")
    }
}
