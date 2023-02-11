package ml.zedlabs.tbd.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.TextField
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ml.zedlabs.tbd.MainViewModel
import ml.zedlabs.tbd.databases.expense_type_db.ExpenseTypeItem
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
import ml.zedlabs.tbd.util.showToast

@OptIn(ExperimentalMaterialApi::class)
@AndroidEntryPoint
class TransactionListFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: TransactionViewModel by activityViewModels()

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
        val scope = rememberCoroutineScope()
        val listItems by viewModel.transactionList.collectAsState()
        val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

        ModalBottomSheetLayout(
            sheetState = bottomState,
            sheetContent = {
                AddTransactionBottomSheet(state = bottomState)
            }) {
            if (listItems is Resource.Success) {
                AddTransactionSubTypeDialog()
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
                        MediumText(
                            text = "Type-> ${item.type}  \nAmount-> ${item.amount}  \nNote-> ${item.note}\n-----------------------",
                            modifier = mod.clickable {
                                //update current selection
                                viewModel.currentTransactionSelection.value =
                                    CurrentItemState.Exists(item)
                                setValues(item.note, item.expenseType, item.type, item.amount.toString())
                                //show bottom sheet
                                scope.launch {
                                    bottomState.show()
                                }
                            }
                        )
                    }
                }
            } else {
                Spacer(
                    modifier = mod
                        .height(1200.dp)
                        .background(color = MaterialTheme.colors.secondary)
                        .fillMaxSize()
                )
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
            modifier = mod.clickable { viewModel.deleteAllFromDb() }
        )
        Spacer24()
        MediumText(
            text = "Add Expense / Income",
            modifier = mod.clickable {
                // open the bottom sheet
                viewModel.currentTransactionSelection.value = CurrentItemState.DoesNotExist
                setValues()
                scope.launch {
                    addTransactionSheetState.show()
                }
            },
            color = MaterialTheme.colors.onSecondary
        )
    }

    private fun setValues(
        noteValue: String = "",
        transactionTypeValue: String = "",
        transactionSubTypeValue: String = "",
        amountValue: String = ""
    ) {
        with(viewModel) {
            amount = amountValue
            note = noteValue
            transactionType.value = transactionTypeValue
            transactionSubType.value = transactionSubTypeValue
        }
    }

    //sub type chip list should be sorted by popularity
    @Composable
    private fun AddTransactionBottomSheet(mod: Modifier = Modifier, state: ModalBottomSheetState) {

//        if (state.isVisible.not()) {
//            // does not initialise the bottom sheet on fragment open
//            // save on initial CPU consumption
//            Spacer(modifier = mod.height(1.dp))
//            return
//        }
        val currentItem = viewModel.currentTransactionSelection.collectAsState()
        val transactionSubTypeList = viewModel.transactionTypeList.collectAsState()
        val ctx = LocalContext.current

        LaunchedEffect(Unit) {
            viewModel.getUsersTransactionTypes()
        }

        if (transactionSubTypeList.value is Resource.Success
            && currentItem.value !is CurrentItemState.Loading
        ) {
            Column(
                modifier = mod
                    .fillMaxWidth(),
                horizontalAlignment = CenterHorizontally
            ) {
                Spacer24()
                ExpenseTypeChips(
                    chips = TransactionType.values().map { it.name },
                )
                Spacer12()
                TransactionSubTypeChipList(
                    chips = transactionSubTypeList.value.data.orEmpty(),
                )
                Spacer12()
                TextField(value = viewModel.note,
                    onValueChange = {
                        viewModel.note = it
                    }
                )
                TextField(
                    value = viewModel.amount,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = {
                        try {
                            viewModel.amount = it
                        } catch (exception: NumberFormatException) {
                            ctx.showToast("Unable to parse as double")
                        }

                    }
                )
                MediumText(text = "Add Transaction",
                    color = MaterialTheme.colors.onSecondary,
                    modifier = mod
                        .clickable {
                            when (currentItem.value) {
                                CurrentItemState.DoesNotExist -> {
                                    // create new transaction
                                    viewModel.createTransaction(
                                        TransactionItem(
                                            expenseType = viewModel.transactionType.value,
                                            timestamp = System.currentTimeMillis(),
                                            note = viewModel.note,
                                            type = viewModel.transactionSubType.value,
                                            amount = viewModel.amount
                                        )
                                    )
                                }

                                is CurrentItemState.Exists -> {
                                    //update existing transaction
                                    if (currentItem.value.transactionItem?.type == viewModel.transactionSubType.value
                                        && currentItem.value.transactionItem?.expenseType == viewModel.transactionType.value
                                        && currentItem.value.transactionItem?.note == viewModel.note
                                        && currentItem.value.transactionItem?.amount == viewModel.amount
                                    ) {
                                        ctx.showToast("No Changes Made yet")
                                    } else {
                                        viewModel.updateTransaction(
                                            TransactionItem(
                                                transactionId = currentItem.value.transactionItem?.transactionId
                                                    ?: return@clickable,
                                                expenseType = viewModel.transactionType.value,
                                                timestamp = currentItem.value.transactionItem?.timestamp
                                                    ?: return@clickable,
                                                note = viewModel.note,
                                                type = viewModel.transactionSubType.value,
                                                amount = viewModel.amount
                                            )
                                        )
                                    }
                                }

                                CurrentItemState.Loading -> Unit
                            }
                        }
                )
            }
        } else {
            Spacer24()
        }

    }

    @Composable
    fun ExpenseTypeChips(chips: List<String>) {
        LazyRow {
            items(chips) { currentItem ->
                Chip(
                    onClick = {
                        viewModel.transactionType.value = currentItem
                    },
                    border = if (viewModel.transactionType.value == currentItem) {
                        BorderStroke(width = 1.dp, color = MaterialTheme.colors.onBackground)
                    } else {
                        null
                    }
                ) {
                    if (viewModel.transactionType.value == currentItem) {
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
    fun TransactionSubTypeChipList(
        chips: List<ExpenseTypeItem>
    ) {
        // get these chips from the transaction type table
        LazyRow {
            items(chips) { currentItem ->
                Chip(
                    onClick = {
                        viewModel.transactionSubType.value = currentItem.type
                    },
                    border = if (viewModel.transactionSubType.value == currentItem.type) {
                        BorderStroke(width = 1.dp, color = MaterialTheme.colors.onBackground)
                    } else {
                        null
                    }
                ) {
                    if (viewModel.transactionSubType.value == currentItem.type) {
                        PrimaryText(text = currentItem.type)
                    } else {
                        SecondaryText(text = currentItem.type)
                    }
                }
                HSpacer12()
            }
            item {
                Chip(
                    onClick = {
                        viewModel.addTypeDialogState.value = true
                    }
                ) {
                    PrimaryText(text = "➕ Add Type")
                }
                HSpacer12()
            }
        }
    }

    @Composable
    fun AddTransactionSubTypeDialog(mod: Modifier = Modifier) {
        var customCategory by remember { mutableStateOf("") }
        if (viewModel.addTypeDialogState.value) {
            AlertDialog(
                backgroundColor = MaterialTheme.colors.secondary,
                onDismissRequest = {
                    viewModel.addTypeDialogState.value = false
                },
                title = {
                    Column(modifier = mod.fillMaxWidth()) {
                        MediumText(
                            modifier = mod
                                .align(CenterHorizontally),
                            color = MaterialTheme.colors.onSecondary,
                            text = "Create New Category"
                        )
                    }
                },
                text = {
                    Column {
                        TextField(
                            value = customCategory,
                            onValueChange = {
                                customCategory = it
                            },
                            maxLines = 1
                        )
                    }
                },
                buttons = {
                    Button(
                        modifier = mod
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 42.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor =
                            MaterialTheme.colors.secondary
                        ),
                        onClick = {
                            // save to db
                            viewModel.createNewTag(customCategory)
                            viewModel.addTypeDialogState.value = false
                        }) {
                        MediumText(text = "Add Category!", color = MaterialTheme.colors.onSecondary)
                    }
                }
            )
        }
    }


}