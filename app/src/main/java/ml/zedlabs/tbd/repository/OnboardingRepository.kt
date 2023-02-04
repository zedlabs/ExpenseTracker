package ml.zedlabs.tbd.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ml.zedlabs.tbd.model.common.Currency
import ml.zedlabs.tbd.ui.onboarding.CURRENCY_DATA
import ml.zedlabs.tbd.ui.onboarding.CurrencyData
import ml.zedlabs.tbd.ui.theme.AppThemeType
import javax.inject.Inject

class OnboardingRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private val APP_CURRENCY = stringPreferencesKey("app_currency")

    private val appCurrency: Flow<String> = dataStore.data.map { preferences ->
        preferences[APP_CURRENCY] ?: "NOT_SET"
    }

    suspend fun updateUserCurrency(symbol: String) {
        dataStore.edit {settings ->
            settings[APP_CURRENCY] = symbol
        }
    }

    fun getCurrentCurrency(): Flow<String> {
        return appCurrency
    }


}