package ml.zedlabs.tbd.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ml.zedlabs.tbd.model.common.Currency
import javax.inject.Inject

class OnboardingRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    fun updateUserCurrency(currency: Currency) {
        // set county in the user data store
    }

    fun getCurrentCurrency(): Flow<String> {
        // get current country or null from the preferences
        return flow {
            ""
        }
    }


}