package ml.zedlabs.tbd.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ml.zedlabs.tbd.databases.expense_type_db.ExpenseTypeDao
import ml.zedlabs.tbd.databases.transaction_db.TransactionDao
import ml.zedlabs.tbd.repository.AppCommonsRepository
import ml.zedlabs.tbd.repository.OnboardingRepository
import ml.zedlabs.tbd.repository.TransactionRepository
import javax.inject.Singleton

const val PREFERENCES_STORE_NAME = "settings"

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAppCommonsRepository(
        dataStore: DataStore<Preferences>
    ): AppCommonsRepository {
        return AppCommonsRepository(
            dataStore,
        )
    }

    @Provides
    @Singleton
    fun provideOnboardingRepository(
        dataStore: DataStore<Preferences>,
    ): OnboardingRepository {
        return OnboardingRepository(
            dataStore
        )
    }

    @Provides
    @Singleton
    fun provideTransactionListRepository(
        transactionDao: TransactionDao,
        expenseTypeDao: ExpenseTypeDao
    ): TransactionRepository {
        return TransactionRepository(transactionDao, expenseTypeDao)
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