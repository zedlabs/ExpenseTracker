package ml.zedlabs.tbd.ui.onboarding

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ml.zedlabs.tbd.model.common.Currency
import ml.zedlabs.tbd.model.common.CurrencyItem
import ml.zedlabs.tbd.model.common.EmojiData
import ml.zedlabs.tbd.repository.OnboardingRepository
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    val selectedCountryCodeState = mutableStateOf<CurrencyItem?>(null)

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
        selectedCountryCodeState.value = currencyData
    }

    fun commitCurrencyToPrefs() {
        val data = selectedCountryCodeState.value?.currencySymbol ?: return
        viewModelScope.launch {
            onboardingRepository.updateUserCurrency(data)
        }
    }

}