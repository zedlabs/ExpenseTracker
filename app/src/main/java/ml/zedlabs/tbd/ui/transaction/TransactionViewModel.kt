package ml.zedlabs.tbd.ui.transaction

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
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
import ml.zedlabs.tbd.databases.expense_type_db.ExpenseTypeItem
import ml.zedlabs.tbd.databases.transaction_db.TransactionItem
import ml.zedlabs.tbd.model.Resource
import ml.zedlabs.tbd.repository.TransactionRepository
import ml.zedlabs.tbd.util.Constants.randomColors
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    val repository: TransactionRepository
) : ViewModel() {

    val addTypeDialogState = mutableStateOf(false)
    val currentTransactionSelection = MutableStateFlow<CurrentItemState>(CurrentItemState.DoesNotExist)
    var note by mutableStateOf("")
    var amount by mutableStateOf("")
    val transactionType = mutableStateOf("")
    val transactionSubType = mutableStateOf("")

    private val _transactionList =
        MutableStateFlow<Resource<List<TransactionItem>>>(Resource.Uninitialised())
    val transactionList = _transactionList.asStateFlow()

    private val _transactionTypeList =
        MutableStateFlow<Resource<List<ExpenseTypeItem>>>(Resource.Uninitialised())
    val transactionTypeList = _transactionTypeList.asStateFlow()

    init {
        getUsersTransactions()
    }

    private fun getUsersTransactions() {
        viewModelScope.launch {
            repository.getAllTransactions()
                .flowOn(Dispatchers.IO)
                .collectLatest {
                    _transactionList.value = Resource.Success(it)
                }
        }
    }

    fun getUsersTransactionTypes() {
        viewModelScope.launch {
            repository.getAllTags()
                .flowOn(Dispatchers.IO)
                .collectLatest {
                    _transactionTypeList.value = Resource.Success(it)
                }
        }
    }


//    fun getTransactionById(transactionId: Int) {
//        currentTransactionSelection.value = Resource.Loading()
//        viewModelScope.launch {
//            val result = withContext(Dispatchers.IO) {
//                repository.getItemByTransactionId(transactionId)
//            }
//            when (result) {
//                null -> {
//                    currentTransactionSelection.value =
//                        Resource.Error(Throwable("Error Reading From Db"))
//                }
//
//                else -> {
//                    currentTransactionSelection.value = Resource.Success(result)
//                }
//            }
//        }
//    }

    fun createTransaction(transactionItem: TransactionItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNewTransaction(transactionItem)
        }
    }

    fun updateTransaction(transactionItem: TransactionItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTransaction(transactionItem)
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

    fun createNewTag(tag: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createNewTag(
                ExpenseTypeItem(
                    type = tag,
                    dotColor = randomColors.random()
                )
            )
        }
    }

    //** fakes **//
//    fun addFakeEntryToTransactionDb() {
//        viewModelScope.launch {
//            repository.addNewTransaction(
//                TransactionItem(
//                    isExpense = (0..100).random() % 2 == 0,
//                    timestamp = (555550L..1000000000L).random(),
//                    note = "this is a sample note attached to a transaction",
//                    type = "Grocery"
//                )
//            )
//        }
//    }

}

sealed class CurrentItemState(val transactionItem: TransactionItem?) {
    class Exists(transactionItem: TransactionItem) : CurrentItemState(transactionItem = transactionItem)
    object DoesNotExist : CurrentItemState(transactionItem = null)
    object Loading : CurrentItemState(transactionItem = null)
}