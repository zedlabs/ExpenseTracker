package ml.zedlabs.tbd.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.TextField
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ml.zedlabs.tbd.MainViewModel
import ml.zedlabs.tbd.databases.transaction_db.TransactionItem
import ml.zedlabs.tbd.model.Resource
import ml.zedlabs.tbd.model.common.TransactionType
import ml.zedlabs.tbd.ui.common.HSpacer12
import ml.zedlabs.tbd.ui.common.LargeText
import ml.zedlabs.tbd.ui.common.MediumText
import ml.zedlabs.tbd.ui.common.PrimaryText
import ml.zedlabs.tbd.ui.common.SecondaryText
import ml.zedlabs.tbd.ui.common.Spacer12
import ml.zedlabs.tbd.ui.common.Spacer24
import ml.zedlabs.tbd.ui.theme.AppThemeType
import ml.zedlabs.tbd.ui.theme.ExpenseTheme

@OptIn(ExperimentalMaterialApi::class)
@AndroidEntryPoint
class TransactionListFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val transactionViewModel: TransactionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ExpenseTheme(
                    appTheme = mainViewModel.appTheme.collectAsState(initial = AppThemeType.Default.name).value
                ) {
                    TransactionListParent()
                }
            }
        }
    }

    @Composable
    fun TransactionListParent(mod: Modifier = Modifier) {

        val addTransactionSheetState =
            rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
        val scaffoldState =
            rememberBottomSheetScaffoldState(bottomSheetState = addTransactionSheetState)
        val scope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            transactionViewModel.getUsersTransactions()
        }
        val listItems by transactionViewModel.transactionList.collectAsState()

        val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

        ModalBottomSheetLayout(
            sheetState = bottomState,
            sheetContent = {
            AddTransactionBottomSheet()
        }) {
            if (listItems is Resource.Success) {
                LazyColumn(
                    modifier = mod
                        .background(color = MaterialTheme.colors.secondary)
                        .fillMaxSize()
                        .padding(horizontal = 12.dp)
                ) {
                    item {
                        TransactionHeaderItem(
                            scope = scope,
                            addTransactionSheetState = bottomState
                        )
                    }
                    items(items = listItems.data.orEmpty()) { item ->
                        MediumText(text = "${item.type}  \n${item.transactionId}  \n${item.note}\n\n\n")
                    }
                }
            } else {
                Spacer(modifier = mod.height(800.dp))
            }
        }
    }

    @Composable
    fun TransactionHeaderItem(
        mod: Modifier = Modifier,
        scope: CoroutineScope,
        addTransactionSheetState: ModalBottomSheetState
    ) {
        Spacer24()
        LargeText(
            text = "Welcome to transaction list",
            color = MaterialTheme.colors.onSecondary,
            modifier = mod.clickable { transactionViewModel.deleteAllFromDb() }
        )
        Spacer24()
        MediumText(
            text = "Add Expense / Income",
            modifier = mod.clickable {
                // open the bottom sheet
                scope.launch {
                    addTransactionSheetState.show()
                }
            },
            color = MaterialTheme.colors.onSecondary
        )
    }

    //sub type chip list should be sorted by popularity
    @Composable
    private fun AddTransactionBottomSheet(mod: Modifier = Modifier) {
        var note by remember { mutableStateOf("") }
        val timestamp = System.currentTimeMillis()
        val transactionType = remember { mutableStateOf("") }
        //this list would be fetched from presaved and editable db_2
        //should also have a color that covers the border
        val transactionSubTypesChips = listOf("🍅 Groceries", "🥗 Food", "🧳 Travel", "⚡ Bills")
        val transactionSubType = remember { mutableStateOf("") }

        Column(
            modifier = mod
                .fillMaxWidth()
                .clickable {
                    //doing nothing here, just to disable touch actions on the bottom sheet
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer24()
            ExpenseTypeChips(
                chips = TransactionType.values().map { it.name },
                transactionType = transactionType
            )
            Spacer12()
            TransactionSubTypeChipList(
                chips = transactionSubTypesChips,
                transactionSubType = transactionSubType
            )
            Spacer12()
            TextField(value = note, onValueChange = {
                note = it
            })
            MediumText(text = "Add Transaction",
                color = MaterialTheme.colors.onSecondary,
                modifier = mod
                    .clickable {
                        transactionViewModel.createTransaction(
                            TransactionItem(
                                isExpense = transactionType.value == TransactionType.Expense.name,
                                timestamp = timestamp,
                                note = note,
                                type = transactionSubType.value
                            )
                        )
                    }
            )
            Spacer24()
        }
    }

    @Composable
    fun ExpenseTypeChips(chips: List<String>, transactionType: MutableState<String>) {
        LazyRow {
            items(chips) { currentItem ->
                Chip(
                    onClick = {
                        transactionType.value = currentItem
                    },
                    border = if (transactionType.value == currentItem) {
                        BorderStroke(width = 1.dp, color = MaterialTheme.colors.onBackground)
                    } else {
                        null
                    }
                ) {
                    if (transactionType.value == currentItem) {
                        PrimaryText(text = currentItem)
                    } else {
                        SecondaryText(text = currentItem)
                    }
                }
                HSpacer12()
            }
        }
    }

    @Composable
    fun TransactionSubTypeChipList(chips: List<String>, transactionSubType: MutableState<String>) {
        LazyRow {
            items(chips) { currentItem ->
                Chip(
                    onClick = {
                        transactionSubType.value = currentItem
                    },
                    border = if (transactionSubType.value == currentItem) {
                        BorderStroke(width = 1.dp, color = MaterialTheme.colors.onBackground)
                    } else {
                        null
                    }
                ) {
                    if (transactionSubType.value == currentItem) {
                        PrimaryText(text = currentItem)
                    } else {
                        SecondaryText(text = currentItem)
                    }
                }
                HSpacer12()
            }
        }
    }


}