package ml.zedlabs.tbd.ui.transaction

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ml.zedlabs.tbd.databases.transaction_db.TransactionItem
import ml.zedlabs.tbd.model.Resource
import ml.zedlabs.tbd.repository.TransactionRepository
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    val repository: TransactionRepository
) : ViewModel() {

    val currentTransactionSelection =
        MutableStateFlow<Resource<TransactionItem>>(Resource.Uninitialised())

    private val _transactionList =
        MutableStateFlow<Resource<List<TransactionItem>>>(Resource.Uninitialised())
    val transactionList = _transactionList.asStateFlow()

    init {
        getUsersTransactions()
    }
    private fun getUsersTransactions(
    ) {
        viewModelScope.launch {
            repository.getAllTransactions()
                .flowOn(Dispatchers.IO)
                .collectLatest {
                    _transactionList.value = Resource.Success(it)
                }
        }
    }


    fun getTransactionById(transactionId: Int) {
        currentTransactionSelection.value = Resource.Loading()
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getItemByTransactionId(transactionId)
            }
            when (result) {
                null -> {
                    currentTransactionSelection.value =
                        Resource.Error(Throwable("Error Reading From Db"))
                }

                else -> {
                    currentTransactionSelection.value = Resource.Success(result)
                }
            }
        }
    }

    fun createTransaction(transactionItem: TransactionItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNewTransaction(transactionItem)
        }
    }

    fun deleteTransaction(transactionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEntry(transactionId = transactionId)
        }
    }

    fun deleteAllFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    //** fakes **//
    fun addFakeEntryToTransactionDb() {
        viewModelScope.launch {
            repository.addNewTransaction(
                TransactionItem(
                    isExpense = (0..100).random() % 2 == 0,
                    timestamp = (555550L..1000000000L).random(),
                    note = "this is a sample note attached to a transaction",
                    type = "Grocery"
                )
            )
        }
    }

}