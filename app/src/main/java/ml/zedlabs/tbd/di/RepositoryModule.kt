package ml.zedlabs.tbd.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ml.zedlabs.tbd.network.JsonApi
import ml.zedlabs.tbd.repository.AppCommonsRepository
import ml.zedlabs.tbd.repository.WebParsingRepository
import ml.zedlabs.tbd.databases.media_db.AddedListDao
import ml.zedlabs.tbd.databases.media_db.AddedListDatabase
import javax.inject.Singleton

const val PREFERENCES_STORE_NAME = "settings"

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAddedListDatabase(
        @ApplicationContext applicationContext: Context
    ): AddedListDatabase {
        return Room
            .databaseBuilder(
                applicationContext,
                AddedListDatabase::class.java, "added-list-db"
            ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideAddedListDao(addedListDatabase: AddedListDatabase): AddedListDao {
        return addedListDatabase.addedListDao()
    }

    @Provides
    @Singleton
    fun provideAppCommonsRepository(
        jsonApi: JsonApi,
        addedListDao: AddedListDao,
        dataStore: DataStore<Preferences>,
    ): AppCommonsRepository {
        return AppCommonsRepository(
            jsonApi,
            addedListDao,
            dataStore,
        )
    }

    @Provides
    @Singleton
    fun provideWebParsingRepository(
    ): WebParsingRepository {
        return WebParsingRepository()
    }


    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = {
                appContext.preferencesDataStoreFile(PREFERENCES_STORE_NAME)
            }
        )

}