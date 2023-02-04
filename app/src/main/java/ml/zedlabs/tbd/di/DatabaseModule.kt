package ml.zedlabs.tbd.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ml.zedlabs.tbd.databases.expense_type_db.ExpenseTypeDao
import ml.zedlabs.tbd.databases.expense_type_db.ExpenseTypeDatabase
import ml.zedlabs.tbd.databases.transaction_db.TransactionDao
import ml.zedlabs.tbd.databases.transaction_db.TransactionDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTransactionDb(
        @ApplicationContext applicationContext: Context
    ): TransactionDatabase {
        return Room
            .databaseBuilder(
                applicationContext,
                TransactionDatabase::class.java, "transaction-list-db"
            ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(transactionDb: TransactionDatabase): TransactionDao {
        return transactionDb.transactionDao()
    }

    @Provides
    @Singleton
    fun provideTypeDb(
        @ApplicationContext applicationContext: Context
    ): ExpenseTypeDatabase {
        return Room
            .databaseBuilder(
                applicationContext,
                ExpenseTypeDatabase::class.java, "expense-type-db"
            ).fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    @Singleton
    fun provideTypeDao(expenseTypeDatabase: ExpenseTypeDatabase): ExpenseTypeDao {
        return expenseTypeDatabase.expenseTypeDao()
    }
}