package ml.zedlabs.tbd.ui.transaction

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.sharp.ArrowDropDown
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ml.zedlabs.tbd.MainViewModel
import ml.zedlabs.tbd.R
import ml.zedlabs.tbd.databases.expense_type_db.ExpenseTypeItem
import ml.zedlabs.tbd.databases.transaction_db.TransactionItem
import ml.zedlabs.tbd.model.Resource
import ml.zedlabs.tbd.model.common.TransactionType
import ml.zedlabs.tbd.ui.common.ButtonRoundedStyle
import ml.zedlabs.tbd.ui.common.DefaultTopButton
import ml.zedlabs.tbd.ui.common.HSpacer12
import ml.zedlabs.tbd.ui.common.MediumText
import ml.zedlabs.tbd.ui.common.PrimaryText
import ml.zedlabs.tbd.ui.common.Spacer12
import ml.zedlabs.tbd.ui.common.Spacer24
import ml.zedlabs.tbd.ui.onboarding.OnboardingViewModel
import ml.zedlabs.tbd.ui.theme.AppThemeType
import ml.zedlabs.tbd.ui.theme.ExpenseTheme
import ml.zedlabs.tbd.ui.theme.greenHome
import ml.zedlabs.tbd.util.showToast

@OptIn(ExperimentalMaterialApi::class)
@AndroidEntryPoint
class TransactionListFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: TransactionViewModel by activityViewModels()
    private val onbViewModel: OnboardingViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
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
        val currency by onbViewModel.localCurrency.collectAsState()

        ModalBottomSheetLayout(
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetState = bottomState,
            sheetContent = {
                AddTransactionBottomSheet(state = bottomState)
            }) {
            if (listItems is Resource.Success) {
                AddTransactionSubTypeDialog()
                MonthSelectionDialog()
                YearSelectionDialog()
                Column {
                    LazyColumn(
                        modifier = mod
                            .background(color = MaterialTheme.colors.secondary)
                            .fillMaxHeight(.92f)
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                    ) {
                        item {
                            TransactionHeaderItem()
                        }
                        if (listItems.data.isNullOrEmpty().not()) {
                            items(items = listItems.data.orEmpty()) { item ->
                                TransactionListItem(item, currency.data.orEmpty()) {
                                    //update current selection
                                    viewModel.currentTransactionSelection.value =
                                        CurrentItemState.Exists(item)
                                    setValues(
                                        item.note,
                                        item.expenseType,
                                        item.type,
                                        item.amount
                                    )
                                    //show bottom sheet
                                    scope.launch {
                                        bottomState.show()
                                    }
                                }
                            }
                        } else {
                            item {
                                EmptyFilterList()
                            }
                        }

                    }
                    Column(
                        modifier = mod
                            .fillMaxSize()
                            .background(MaterialTheme.colors.secondary),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        TransactionBottomSticky(
                            scope = scope,
                            addTransactionSheetState = bottomState
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
    fun EmptyFilterList() {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer24()
            Spacer24()
            Spacer24()
            Image(
                painter = painterResource(id = R.drawable.search_empty),
                contentDescription = "searching",
                alignment = Alignment.Center
            )
            Spacer24()
            PrimaryText(
                text = "No Transactions found for the selected time period!",
                align = TextAlign.Center,
                color = MaterialTheme.colors.onSecondary
            )
        }
    }

    private val rowModifier = Modifier
        .fillMaxWidth()
        .padding(top = 6.dp, start = 6.dp, end = 6.dp)

    private val RowScope.rowItemMod: Modifier
        get() = Modifier.align(Alignment.CenterVertically)

    //to add here: transaction time, income confetti, delete option
    @Composable
    private fun TransactionListItem(item: TransactionItem, currency: String, onClick: () -> Unit) {
        val isExpense: Boolean = (item.expenseType == TransactionType.Expense.name)
        val operator = if (isExpense) "-" else "+"
        val connectorText = if (isExpense) "on" else "from"
        Column(modifier = Modifier.clickable {
            onClick.invoke()
        }) {
            Row(modifier = rowModifier) {
//            MediumText(
//                text = transactionEmoji,
//                fontWeight = FontWeight.Bold,
//                fontSize = 20.sp
//            )
                MediumText(
                    modifier = rowItemMod,
                    text = "$operator$currency${item.amount}",
                    color = if (isExpense) MaterialTheme.colors.background else greenHome,
                    fontWeight = FontWeight.Bold
                )

                MediumText(
                    modifier = rowItemMod,
                    text = if (item.type.isEmpty()) "" else " $connectorText ${item.type}",
                    color = MaterialTheme.colors.onSecondary
                )
            }
            if (item.note.isNotEmpty()) {
                MediumText(
                    modifier = rowModifier.padding(start = 6.dp),
                    text = item.note,
                    color = MaterialTheme.colors.onSecondary.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                )
            }
            Spacer(
                modifier = rowModifier
                    .background(Color.Gray.copy(alpha = 0.4f))
                    .fillMaxWidth()
                    .height(1.dp)
            )
        }
    }

    @Composable
    fun MonthSelectionDialog(mod: Modifier = Modifier, context: Context = LocalContext.current) {
        if (viewModel.monthSelectionDialogState.value) {
            AlertDialog(
                backgroundColor = MaterialTheme.colors.secondary,
                onDismissRequest = {
                    viewModel.monthSelectionDialogState.value = false
                },
                title = {
                    Column(modifier = mod.fillMaxWidth()) {
                        MediumText(
                            modifier = mod
                                .align(CenterHorizontally),
                            color = MaterialTheme.colors.onSecondary,
                            text = "Select Month",
                            fontSize = 22.sp,
                            style = TextStyle(textDecoration = TextDecoration.Underline),
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                text = {
                    Column {
                        listOf(
                            "All",
                            "January",
                            "February",
                            "March",
                            "April",
                            "May",
                            "June",
                            "July",
                            "August",
                            "September",
                            "October",
                            "November",
                            "December"
                        ).forEach {
                            MediumText(
                                text = it,
                                modifier = mod
                                    .padding(vertical = 6.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable {
                                        viewModel.selectedMonth.value =
                                            if (it == "All") null else it
                                        viewModel.getUsersTransactions()
                                        viewModel.monthSelectionDialogState.value = false
                                    },
                                color = MaterialTheme.colors.onSecondary,
                                fontSize = 18.sp,
                            )
                        }
                    }
                },
                buttons = {
                    // no button needed
                }
            )
        }
    }

    @Composable
    fun YearSelectionDialog(mod: Modifier = Modifier, context: Context = LocalContext.current) {
        if (viewModel.yearSelectionDialogState.value) {
            AlertDialog(
                backgroundColor = MaterialTheme.colors.secondary,
                onDismissRequest = {
                    viewModel.yearSelectionDialogState.value = false
                },
                title = {
                    Column(modifier = mod.fillMaxWidth()) {
                        MediumText(
                            modifier = mod
                                .align(CenterHorizontally),
                            color = MaterialTheme.colors.onSecondary,
                            text = "Select Year",
                            fontSize = 22.sp,
                            style = TextStyle(textDecoration = TextDecoration.Underline),
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                text = {
                    Column {
                        viewModel.pastSixYearsList.forEach {
                            MediumText(
                                text = it,
                                modifier = mod
                                    .padding(vertical = 6.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable {
                                        viewModel.selectedYear.value = if (it == "All") null else it
                                        viewModel.getUsersTransactions()
                                        viewModel.yearSelectionDialogState.value = false
                                    },
                                color = MaterialTheme.colors.onSecondary,
                                fontSize = 18.sp,
                            )
                        }
                    }
                },
                buttons = {
                    // no buttons needed RM
                }
            )
        }
    }

    @Composable
    fun TransactionHeaderItem(
        mod: Modifier = Modifier,
    ) {
        Spacer12()
        Column(
            modifier = mod.fillMaxWidth()
        ) {
            Row {
                DefaultTopButton(
                    imageVector = Icons.Rounded.ArrowBack,
                    bgColor = MaterialTheme.colors.onSecondary,
                    itemColor = MaterialTheme.colors.onPrimary
                ) {
                    view?.findNavController()?.navigateUp()
                }
                MediumText(
                    modifier = mod
                        .align(Bottom)
                        .padding(start = 26.dp),
                    text = "Your Transactions for",
                    color = MaterialTheme.colors.onSecondary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 26.sp
                )
            }
            Spacer24()
            Row(
                modifier = mod
                    .fillMaxWidth()
                    .padding(horizontal = 38.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ButtonRoundedStyle(
                    click = {
                        viewModel.monthSelectionDialogState.value = true
                    },
                    text = viewModel.selectedMonth.value ?: "All",
                    vector = Icons.Sharp.ArrowDropDown

                )
                ButtonRoundedStyle(
                    click = {
                        viewModel.yearSelectionDialogState.value = true
                    },
                    text = viewModel.selectedYear.value ?: "All",
                    vector = Icons.Sharp.ArrowDropDown
                )
            }
        }
        Spacer24()
    }

    @Composable
    fun TransactionBottomSticky(
        mod: Modifier = Modifier,
        scope: CoroutineScope,
        addTransactionSheetState: ModalBottomSheetState
    ) {
        Box(
            modifier = mod
                .fillMaxSize()
                .background(MaterialTheme.colors.secondary)
        ) {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colors.onSecondary)
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .align(Center)
                    .clickable {
                        //    open the bottom sheet
                        viewModel.currentTransactionSelection.value = CurrentItemState.DoesNotExist
                        setValues()
                        scope.launch {
                            addTransactionSheetState.show()
                        }
                    }
            ) {
                MediumText(
                    text = "Add Expense / Income",
                    color = MaterialTheme.colors.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                HSpacer12()
                Icon(
                    imageVector = Icons.Rounded.Add,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .size(18.dp),
                    contentDescription = "create new transaction"
                )
            }
        }
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
        val currency by onbViewModel.localCurrency.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.getUsersTransactionTypes()
        }

        if (transactionSubTypeList.value is Resource.Success
            && currentItem.value !is CurrentItemState.Loading
        ) {
            Column(
                modifier = mod
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.secondary),
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
                TextField(
                    modifier = mod.padding(horizontal = 20.dp),
                    value = viewModel.amount,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ),
                    placeholder = {
                        PrimaryText(
                            text = "💸 Enter Amount in ${currency.data.orEmpty()}",
                            color = MaterialTheme.colors.onSecondary.copy(alpha = 0.4f),
                            fontSize = 16.sp,
                            align = TextAlign.Center
                        )
                    },
                    singleLine = true,
                    onValueChange = {
                        try {
                            viewModel.amount = it
                        } catch (exception: NumberFormatException) {
                            ctx.showToast("Unable to parse as double")
                        }

                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.onSecondary,
                        cursorColor = MaterialTheme.colors.onSecondary,
                        backgroundColor = MaterialTheme.colors.onSecondary.copy(alpha = 0.1f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer12()
                TextField(

                    modifier = mod.padding(horizontal = 20.dp),
                    value = viewModel.note,
                    onValueChange = {
                        viewModel.note = it
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.onSecondary,
                        cursorColor = MaterialTheme.colors.onSecondary,
                        backgroundColor = MaterialTheme.colors.onSecondary.copy(alpha = 0.1f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = {
                        PrimaryText(
                            text = "📝 Note (Optional)",
                            color = MaterialTheme.colors.onSecondary.copy(alpha = 0.4f),
                            fontSize = 16.sp,
                            align = TextAlign.Center
                        )
                    },
                )
                Spacer24()

                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colors.onSecondary)
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .clickable {
                            when (currentItem.value) {
                                CurrentItemState.DoesNotExist -> {
                                    val ts = System.currentTimeMillis()
                                    // create new transaction
                                    viewModel.createTransaction(
                                        TransactionItem(
                                            expenseType = viewModel.transactionType.value,
                                            timestamp = ts,
                                            note = viewModel.note,
                                            type = viewModel.transactionSubType.value,
                                            amount = viewModel.amount,
                                            date = viewModel.getFormattedIntegerDateFromTimeStamp(
                                                ts
                                            )
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
                                                amount = viewModel.amount,
                                                date = currentItem.value.transactionItem?.date
                                                    ?: return@clickable
                                            )
                                        )
                                    }
                                }

                                CurrentItemState.Loading -> Unit
                            }
                        }
                ) {
                    MediumText(
                        text = if (currentItem.value is CurrentItemState.Exists) "Update Transaction" else "Add Transaction",
                        color = MaterialTheme.colors.primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    HSpacer12()
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .size(16.dp),
                        contentDescription = "create new transaction"
                    )
                }
                Spacer24()
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
                    colors = ChipDefaults.chipColors(
                        backgroundColor = MaterialTheme.colors.onSecondary.copy(
                            alpha = 0.2f
                        )
                    ),
                    onClick = {
                        viewModel.transactionType.value = currentItem
                    },
                    border = if (viewModel.transactionType.value == currentItem) {
                        BorderStroke(width = 2.dp, color = MaterialTheme.colors.onBackground)
                    } else {
                        null
                    }
                ) {
                    PrimaryText(text = currentItem, color = MaterialTheme.colors.onSecondary)
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
                    colors = ChipDefaults.chipColors(
                        backgroundColor = MaterialTheme.colors.onSecondary.copy(
                            alpha = 0.2f
                        )
                    ),
                    onClick = {
                        viewModel.transactionSubType.value = currentItem.type
                    },
                    border = if (viewModel.transactionSubType.value == currentItem.type) {
                        BorderStroke(width = 2.dp, color = MaterialTheme.colors.onBackground)
                    } else {
                        null
                    }
                ) {
                    PrimaryText(text = currentItem.type, color = MaterialTheme.colors.onSecondary)
                }
                HSpacer12()
            }
            item {
                Chip(
                    colors = ChipDefaults.chipColors(
                        backgroundColor = MaterialTheme.colors.onSecondary.copy(
                            alpha = 0.2f
                        )
                    ),
                    onClick = {
                        viewModel.addTypeDialogState.value = true
                    }
                ) {
                    PrimaryText(text = "➕ Add Type", color = MaterialTheme.colors.onSecondary)
                }
                HSpacer12()
            }
        }
    }

    private fun showDeleteTagDialog() {
        Toast.makeText(requireActivity(), "deleted!", Toast.LENGTH_SHORT).show()
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
                title = {},
                text = {
                    Column {
                        TextField(
                            value = customCategory,
                            onValueChange = {
                                customCategory = it
                            },
                            maxLines = 1,
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = MaterialTheme.colors.onSecondary,
                                cursorColor = MaterialTheme.colors.onSecondary,
                                backgroundColor = MaterialTheme.colors.onSecondary.copy(alpha = 0.1f),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(12.dp),
                            placeholder = {
                                PrimaryText(
                                    text = "📝 Create a new tag, eg:-\n\n\uD83D\uDEA3 Boat Repairs\n\uD83D\uDC1F Fish Food\n\uD83C\uDFC4 Surfing",
                                    color = MaterialTheme.colors.onSecondary.copy(alpha = 0.4f),
                                    fontSize = 16.sp,
                                    align = TextAlign.Start
                                )
                            },
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
                            MaterialTheme.colors.onSecondary
                        ),
                        onClick = {
                            // save to db
                            viewModel.createNewTag(customCategory)
                            viewModel.addTypeDialogState.value = false
                        }) {
                        MediumText(
                            text = "Add Category!",
                            color = MaterialTheme.colors.primary,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            )
        }
    }


}