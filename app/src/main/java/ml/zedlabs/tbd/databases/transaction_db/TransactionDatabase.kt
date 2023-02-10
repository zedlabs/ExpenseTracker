package ml.zedlabs.tbd.databases.transaction_db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TransactionItem::class], version = 2)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}