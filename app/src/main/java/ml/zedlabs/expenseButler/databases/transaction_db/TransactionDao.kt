package ml.zedlabs.expenseButler.databases.transaction_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactionitem ORDER BY timestamp DESC")
    fun getAll(): Flow<List<TransactionItem>>

    @Query("SELECT * FROM transactionitem WHERE transaction_id IN (:transactionIds)")
    suspend fun loadAllByIds(transactionIds: IntArray): List<TransactionItem>?

    @Query("SELECT * FROM transactionitem WHERE transaction_id = :transactionId")
    suspend fun getById(transactionId: Int): TransactionItem?

    @Insert
    suspend fun insertAll(vararg items: TransactionItem)

    @Query("DELETE FROM transactionitem WHERE transaction_id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM transactionitem")
    suspend fun deleteAll()

    @Update(entity = TransactionItem::class)
    suspend fun update(obj: TransactionItem)

    @Query("SELECT * FROM transactionitem WHERE date = :date")
    suspend fun getByDate(date: String): List<TransactionItem>?
}