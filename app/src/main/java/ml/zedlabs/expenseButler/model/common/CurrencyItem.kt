package ml.zedlabs.expenseButler.model.common

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
