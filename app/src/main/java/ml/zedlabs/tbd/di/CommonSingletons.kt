import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("settings")
val NIGHT_MODE = booleanPreferencesKey("night_mode")

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {

    private val settingsDataStore = appContext.dataStore

    suspend fun setThemeMode(mode: Boolean) {
        settingsDataStore.edit { settings ->
            settings[NIGHT_MODE] = mode
        }
    }

    suspend fun updateValue() {
        settingsDataStore.edit { settings ->
            settings[NIGHT_MODE] = !(settings[NIGHT_MODE] ?: false)
        }
    }

    val themeMode: Flow<Boolean> = settingsDataStore.data.map { preferences ->
        preferences[NIGHT_MODE] ?: false
    }


}