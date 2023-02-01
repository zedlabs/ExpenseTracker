package ml.zedlabs.tbd.ui.onboarding

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ml.zedlabs.tbd.model.common.Currency
import ml.zedlabs.tbd.model.common.CurrencyItem
import ml.zedlabs.tbd.model.common.EmojiData
import ml.zedlabs.tbd.repository.OnboardingRepository
import java.util.Collections.copy
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val NO_SELECTION = "NO_SELECTION"
    val selectedCountryCodeState = mutableStateOf(NO_SELECTION)

    val countryList = CURRENCY_DATA.flatMap { (key, value) ->
        value.countryCodes.map {
            val countryName = COUNTRY_DATA[it]?.name.orEmpty()
            val flagKey = countryName.lowercase().replace(" ", "_")
            val flag = EmojiData.DATA[flagKey] ?: "🏳️"

            CurrencyItem(
                currencySymbol = value.symbol,
                flag = flag,
                countryName = countryName,
                countryCode = it,
                currency = Currency(
                    key,
                    it
                )
            )
        }
    }.sortedBy { it.countryName }

    fun countrySelected(currencyData: CurrencyItem) {
        val TAG = "ONBVM"
        Log.e(TAG, "countrySelected: ${currencyData.countryCode}")
        selectedCountryCodeState.value = currencyData.countryCode
        // saves the currency in the preferences
        // the app will default to this currency in the future
    }

}