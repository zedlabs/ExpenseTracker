package ml.zedlabs.expenseButler.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ml.zedlabs.expenseButler.ui.theme.AppThemeType
import javax.inject.Inject

class AppCommonsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {

    private val NIGHT_MODE = booleanPreferencesKey("night_mode")
    private val ACTIVE_SUB = booleanPreferencesKey("active_sub")
    private val APP_THEME = stringPreferencesKey("app_theme")

    private val themeMode: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[NIGHT_MODE] ?: false
    }
    private val subState: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[ACTIVE_SUB] ?: false
    }

    private val appTheme: Flow<String> = dataStore.data.map { preferences ->
        preferences[APP_THEME] ?: AppThemeType.Default.name
    }

    fun getTheme(): Flow<Boolean> {
        return themeMode
    }

    fun getSubState(): Flow<Boolean> {
        return subState
    }

    fun getAppTheme(): Flow<String> {
        return appTheme
    }

    suspend fun updateTheme() {
        dataStore.edit { settings ->
            settings[NIGHT_MODE] = !(settings[NIGHT_MODE] ?: false)
        }
    }

    suspend fun setSubState(active: Boolean) {
        dataStore.edit { settings ->
            settings[ACTIVE_SUB] = active
        }
    }

    suspend fun updateAppTheme(appTheme: String) {
        dataStore.edit { settings ->
            settings[APP_THEME] = appTheme
        }
    }

    /**
     * Sample functions for DB queries
     **/

//   fun getUserAddedList(
//        mediaType: MediaType?,
//        watchStatus: WatchStatus?
//    ) =
//        addedListDao.getAll().flowOn(Dispatchers.Main).map { userList ->
//            userList.map { it.asDomainModel() }
//                .filter { if (mediaType == null) true else mediaType == it.type }
//                .filter { if (watchStatus == null) true else watchStatus == it.watchStatus }
//        }.conflate()
//
//    // can add a return value sealed result here to indicate if the object has been inserted or not
//    suspend fun addToUserAddedList(item: AddedList) {
//        // return as item already exists
//        if (addedListDao.getById(item.dbMediaId) != null) return
//        addedListDao.insertAll(item.asDataModel())
//    }
//
//    suspend fun deleteAllEntries() {
//        daysStatsDao.deleteAll()
//    }
//
//    suspend fun getItemByMediaId(uuid: Int): AddedList? {
//        val item = addedListDao.getById(uuid) ?: return null
//        return item.asDomainModel()
//    }
//
//    suspend fun updateWatchStatus(update: WatchStatusUpdate) {
//        addedListDao.update(
//            ml.zedlabs.data.local_db.WatchStatusUpdate(
//                mediaId = update.mediaId,
//                watchStatus = update.watchStatus,
//                userRating = update.userRating,
//                seasonsWatched = update.seasonsWatched,
//                episodesWatched = update.episodesWatched,
//                lastUpdateMs = update.lastUpdateMs
//            )
//        )
//    }
//
//    suspend fun deleteEntry(mediaId: Int) {
//        addedListDao.delete(mediaId)
//    }

}