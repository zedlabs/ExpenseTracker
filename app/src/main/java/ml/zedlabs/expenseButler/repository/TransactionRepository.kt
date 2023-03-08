package ml.zedlabs.expenseButler.repository

import ml.zedlabs.expenseButler.databases.expense_type_db.ExpenseTypeDao
import ml.zedlabs.expenseButler.databases.expense_type_db.ExpenseTypeItem
import ml.zedlabs.expenseButler.databases.transaction_db.TransactionDao
import ml.zedlabs.expenseButler.databases.transaction_db.TransactionItem
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao,
    private val expenseTypeDao: ExpenseTypeDao,
) {

    // can add filters for price range here
    fun getAllTransactions() = transactionDao.getAll()

    fun getAllTags() = expenseTypeDao.getAll()
    suspend fun addNewTransaction(item: TransactionItem) {
        transactionDao.insertAll(item)
    }

    suspend fun updateTransaction(item: TransactionItem) {
        transactionDao.update(item)
    }

    suspend fun getItemByTransactionId(transactionId: Int): TransactionItem? {
        return transactionDao.getById(transactionId)
    }

    suspend fun deleteEntry(transactionId: Int) {
        transactionDao.delete(transactionId)
    }

    suspend fun deleteAll() {
        transactionDao.deleteAll()
    }

    suspend fun createNewTag(item: ExpenseTypeItem) {
        expenseTypeDao.insertAll(item)
    }

    suspend fun getByDate(date: String): List<TransactionItem>? {
        return transactionDao.getByDate(date)
    }

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




}