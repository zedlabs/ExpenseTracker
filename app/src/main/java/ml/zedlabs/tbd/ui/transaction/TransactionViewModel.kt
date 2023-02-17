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
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ml.zedlabs.tbd.databases.expense_type_db.ExpenseTypeItem
import ml.zedlabs.tbd.databases.transaction_db.TransactionItem
import ml.zedlabs.tbd.model.Resource
import ml.zedlabs.tbd.repository.TransactionRepository
import ml.zedlabs.tbd.util.Constants.randomColors
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    val repository: TransactionRepository
) : ViewModel() {

    val addTypeDialogState = mutableStateOf(false)
    val monthSelectionDialogState = mutableStateOf(false)
    val yearSelectionDialogState = mutableStateOf(false)
    val currentTransactionSelection =
        MutableStateFlow<CurrentItemState>(CurrentItemState.DoesNotExist)
    var note by mutableStateOf("")
    var amount by mutableStateOf("")
    val transactionType = mutableStateOf("")
    val transactionSubType = mutableStateOf("")

    val selectedYear = mutableStateOf<String?>(getYearFromTimestamp(System.currentTimeMillis()))
    val selectedMonth = mutableStateOf<String?>(getMonthFromTimestamp(System.currentTimeMillis()))

    private val _transactionList =
        MutableStateFlow<Resource<List<TransactionItem>>>(Resource.Uninitialised())
    val transactionList = _transactionList.asStateFlow()

    private val _transactionTypeList =
        MutableStateFlow<Resource<List<ExpenseTypeItem>>>(Resource.Uninitialised())
    val transactionTypeList = _transactionTypeList.asStateFlow()

    val lastTenDayTransactionPairs = mutableStateOf<List<Pair<String, Double>>>(listOf())

    init {
        getTransactionPairsForLastTenDays()
        getUsersTransactions()
    }

    fun getUsersTransactions() {
        viewModelScope.launch {
            repository.getAllTransactions().map {
                it.filter { item ->
                    if (selectedYear.value == null)
                        true
                    else
                        selectedYear.value == getYearFromTimestamp(item.timestamp)
                }.filter { item ->
                    if (selectedMonth.value == null)
                        true
                    else
                        selectedMonth.value == getMonthFromTimestamp(item.timestamp)
                }
            }
                .flowOn(Dispatchers.Main)
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

    private fun getDayFromTimestamp(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "Sun"
            Calendar.MONDAY -> "Mon"
            Calendar.TUESDAY -> "Tue"
            Calendar.WEDNESDAY -> "Wed"
            Calendar.THURSDAY -> "Thu"
            Calendar.FRIDAY -> "Fri"
            Calendar.SATURDAY -> "Sat"
            else -> "Inv. day"
        }
    }

    private fun getMonthFromTimestamp(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return when (calendar.get(Calendar.MONTH)) {
            Calendar.JANUARY -> "January"
            Calendar.FEBRUARY -> "February"
            Calendar.MARCH -> "March"
            Calendar.APRIL -> "April"
            Calendar.MAY -> "May"
            Calendar.JUNE -> "June"
            Calendar.JULY -> "July"
            Calendar.AUGUST -> "August"
            Calendar.SEPTEMBER -> "September"
            Calendar.OCTOBER -> "October"
            Calendar.NOVEMBER -> "November"
            Calendar.DECEMBER -> "December"
            else -> "Inv. Month"
        }
    }

    val pastSixYearsList: List<String> by lazy {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val yearList = mutableListOf<String>()
        yearList.add(0, "All")
        (0..5).forEach {
            yearList.add(it + 1, year.minus(it).toString())
        }
        yearList
    }

    fun getYearFromTimestamp(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return calendar.get(Calendar.YEAR).toString()
    }

    private fun getTransactionPairsForLastTenDays() {
        viewModelScope.launch {
            val list = mutableListOf<Pair<String, Double>>()
            getLastTenDaysAsIntArray().forEach { element ->
                var sum = 0.0
                repository.getByDate(element)?.forEach {
                    val amount: Double = try {
                        it.amount.toDouble()
                    } catch (exception: Exception) {
                        0.0
                    }
                    sum += amount
                }
                list.add(Pair(getChartFormattedDate(element), sum))
            }
            lastTenDayTransactionPairs.value = list
        }
    }

    fun getLastTenDaysAsIntArray(): List<String> {
        val today = System.currentTimeMillis()
        val dateFormatArray = mutableListOf<String>()
        dateFormatArray.add(0, getFormattedIntegerDateFromTimeStamp(today))
        val tfHrsInMs = 24 * 60 * 60 * 1000 // 1 day in ms
        for (i in 1..9) {
            dateFormatArray.add(
                i,
                getFormattedIntegerDateFromTimeStamp(today.minus(i.times(tfHrsInMs)))
            )
        }
        return dateFormatArray.asReversed()
    }

    fun getFormattedIntegerDateFromTimeStamp(timestamp: Long): String {
        return SimpleDateFormat("MMddyyyy").format(Date(timestamp))
    }

    private fun getChartFormattedDate(date: String): String {
        val df = SimpleDateFormat("MMddyyyy")
        val newDate = df.parse(date)
        return SimpleDateFormat("dd-MMMM").format(newDate)
    }

}

sealed class CurrentItemState(val transactionItem: TransactionItem?) {
    class Exists(transactionItem: TransactionItem) :
        CurrentItemState(transactionItem = transactionItem)

    object DoesNotExist : CurrentItemState(transactionItem = null)
    object Loading : CurrentItemState(transactionItem = null)
}