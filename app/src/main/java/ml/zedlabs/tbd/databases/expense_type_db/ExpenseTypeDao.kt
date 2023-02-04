package ml.zedlabs.tbd.databases.expense_type_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ml.zedlabs.data.local_db.AddedList
import ml.zedlabs.data.local_db.WatchStatusUpdate

@Dao
interface ExpenseTypeDao {
    @Query("SELECT * FROM expensetypeitem")
    fun getAll(): Flow<List<ExpenseTypeItem>>

    @Query("SELECT * FROM expensetypeitem WHERE type_id IN (:itemIds)")
    suspend fun loadAllByIds(itemIds: IntArray): List<ExpenseTypeItem>?

    @Query("SELECT * FROM expensetypeitem WHERE type_id = :itemId")
    suspend fun getById(itemId: Int): ExpenseTypeItem?

    @Insert
    suspend fun insertAll(vararg items: ExpenseTypeItem)

    @Query("DELETE FROM expensetypeitem WHERE type_id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM expensetypeitem")
    suspend fun deleteAll()

}