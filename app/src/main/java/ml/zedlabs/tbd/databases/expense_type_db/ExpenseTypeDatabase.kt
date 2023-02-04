package ml.zedlabs.tbd.databases.expense_type_db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ExpenseTypeItem::class], version = 1)
abstract class ExpenseTypeDatabase : RoomDatabase() {
    abstract fun expenseTypeDao(): ExpenseTypeDao
}