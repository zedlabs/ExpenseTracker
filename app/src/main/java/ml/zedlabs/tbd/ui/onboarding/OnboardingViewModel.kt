package ml.zedlabs.tbd.ui.onboarding

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ml.zedlabs.tbd.databases.transaction_db.TransactionItem
import ml.zedlabs.tbd.model.Resource
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
    val _localCurrency = MutableStateFlow<Resource<String>>(Resource.Uninitialised())
    val localCurrency = _localCurrency.asStateFlow()

    init {
        getCurrencyFromLocalStorage()
    }

    private fun getCurrencyFromLocalStorage() {
        _localCurrency.value = Resource.Loading()
        val currencyValue = onboardingRepository.getCurrentCurrency()
        viewModelScope.launch {
            currencyValue.collectLatest {
                _localCurrency.value = when (it) {
                    "" -> Resource.Error(Throwable("Invalid currency value, set again"))
                    "NOT_SET" -> Resource.Error(Throwable("currency not set"))
                    else -> Resource.Success(it)
                }
            }
        }
    }

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