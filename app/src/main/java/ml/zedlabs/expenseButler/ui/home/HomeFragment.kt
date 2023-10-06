package ml.zedlabs.expenseButler.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import ml.zedlabs.expenseButler.MainActivity
import ml.zedlabs.expenseButler.MainViewModel
import ml.zedlabs.expenseButler.R
import ml.zedlabs.expenseButler.model.Resource
import ml.zedlabs.expenseButler.model.common.TransactionType
import ml.zedlabs.expenseButler.ui.common.DefaultTopButton
import ml.zedlabs.expenseButler.ui.common.HSpacer12
import ml.zedlabs.expenseButler.ui.common.LargeText
import ml.zedlabs.expenseButler.ui.common.MediumText
import ml.zedlabs.expenseButler.ui.common.Spacer12
import ml.zedlabs.expenseButler.ui.common.Spacer24
import ml.zedlabs.expenseButler.ui.onboarding.OnboardingViewModel
import ml.zedlabs.expenseButler.ui.theme.AppThemeType
import ml.zedlabs.expenseButler.ui.theme.ExpenseTheme
import ml.zedlabs.expenseButler.ui.theme.greenHome
import ml.zedlabs.expenseButler.ui.transaction.TransactionViewModel
import ml.zedlabs.expenseButler.ui.transaction.yEmpty
import ml.zedlabs.expenseButler.util.changeStatusBarColor

class HomeFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val onboardingViewModel: OnboardingViewModel by activityViewModels()
    private val transactionViewModel: TransactionViewModel by activityViewModels()
    private val rowModifier = Modifier
        .fillMaxWidth()
        .padding(top = 6.dp, start = 6.dp, end = 6.dp)

    override fun onResume() {
        super.onResume()
        transactionViewModel.getTransactionPairsForLastWeek()
    }

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
                    Home()
                }
            }
        }
    }

    @Composable
    fun Home() {
        (activity as? MainActivity?)?.changeStatusBarColor(
            MaterialTheme.colors.background.toArgb(),
            false
        )
        Column {
            // Top 65% is red
            val transations = transactionViewModel.transactionList.collectAsState()
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .fillMaxHeight(.65f)
            ) {
                HomeTopSection()
            }
            // Bottom 35% is off-white
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.secondary)
                    .fillMaxHeight()
            ) {
                HomeBottomSection()
            }
        }
    }

    @Composable
    fun HomeTopSection(
        mod: Modifier = Modifier
    ) {
        val currency by onboardingViewModel.localCurrency.collectAsState()
        val largestTransactionPastWeek = transactionViewModel.largestExpensePastWeek.value
        Column(
            modifier = mod
                .padding(horizontal = 12.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Spacer24()
                DefaultTopButton(imageVector = Icons.Rounded.Person) {
                    redirectToProfile()
                }
                LargeText(text = "          This chart tracks your expenses over the past week.")
                Spacer12()
                if (largestTransactionPastWeek != null) {
                    MediumText(
                        text = "Your largest expense was of ${currency.data}${largestTransactionPastWeek.amount} on ${largestTransactionPastWeek.type}",
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (transactionViewModel.lastTenDayTransactionPairs.value is Resource.Success) {
                    if (transactionViewModel.lastTenDayTransactionPairs.value.data?.yEmpty() == true) {
                        Column(modifier = mod.fillMaxWidth()) {
                            Spacer12()
                            Image(
                                painterResource(id = R.drawable.finding_person),
                                "",
                                modifier = mod
                                    .fillMaxWidth()
                                    .width(160.dp)
                                    .height(160.dp),
                            )
                            Spacer12()
                            MediumText(
                                text = "No transactions found to create chart!",
                                color = MaterialTheme.colors.primary,
                                modifier = mod.align(CenterHorizontally),
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        LastTenDaysSpendChart(lastTenDayExpenses = transactionViewModel.lastTenDayTransactionPairs.value.data.orEmpty())
                    }
                }
                Spacer12()
            }
            Column(modifier = mod.fillMaxWidth()) {
                Row(
                    modifier = mod
                        .weight(1f, false)
                        .align(Alignment.End)
                        .background(MaterialTheme.colors.onSecondary)
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .clickable {
                            redirectToTransactionList()
                        }
                ) {
                    MediumText(
                        text = "Add Expense / Income",
                        color = MaterialTheme.colors.primary,
                        fontSize = 14.sp
                    )
                    HSpacer12()
                    Icon(
                        imageVector = Icons.Rounded.ArrowForward,
                        tint = MaterialTheme.colors.primary,
                        modifier = mod
                            .size(18.dp)
                            .rotate(-45f),
                        contentDescription = "Add expense screen redirect button"
                    )
                }
                Spacer24()
            }

        }
    }

    @Composable
    fun HomeBottomSection() {
        val tl by transactionViewModel.lastFiveTransactions.collectAsState()
        val currency by onboardingViewModel.localCurrency.collectAsState()
        if (tl is Resource.Success) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MediumText(
                            modifier = Modifier.padding(top = 28.dp, bottom = 12.dp),
                            fontSize = 22.sp,
                            text = "Last 5 Transactions:",
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colors.onSecondary
                        )
                        Row(
                            modifier = Modifier
                                .padding(top = 24.dp, bottom = 12.dp)
                                .background(MaterialTheme.colors.onSecondary)
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                                .clickable {
                                    redirectToTransactionList()
                                }
                        ) {
                            MediumText(
                                text = "All",
                                color = MaterialTheme.colors.primary,
                                fontSize = 14.sp
                            )
                            HSpacer12()
                            Icon(
                                imageVector = Icons.Rounded.ArrowForward,
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier
                                    .size(18.dp)
                                    .rotate(45f),
                                contentDescription = "Add expense screen redirect button"
                            )
                        }
                    }

                }
                val subList = tl.data?.take(5).orEmpty()
                if (subList.isNotEmpty()) {
                    itemsIndexed(items = subList) { index, item ->
                        BottomTransactionItem(
                            item.amount,
                            item.type,
                            item.expenseType == TransactionType.Expense.name,
                            currency.data.orEmpty(),
                            item.note,
                            index != subList.lastIndex
                        )
                    }
                } else {
                    item {
                        MediumText(
                            modifier = Modifier.padding(24.dp),
                            text = "Your most recent transactions will appear here, a more detailed analysis can be found on the all expenses page",
                            color = MaterialTheme.colors.onSecondary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraLight,
                            style = TextStyle(textDecoration = TextDecoration.Underline),
                        )
                    }
                }
                item {
                    Spacer24()
                }

            }
        }

    }

    val RowScope.rowItemMod: Modifier
        get() = Modifier.align(CenterVertically)

    @Composable
    fun BottomTransactionItem(
        cost: String,
        type: String,
        isExpense: Boolean,
        currency: String,
        note: String,
        drawSep: Boolean
    ) {
        val operator = if (isExpense) "-" else "+"
        val connectorText = if (isExpense) "on" else "from"
        Column {
            Row(modifier = rowModifier) {
//            MediumText(
//                text = transactionEmoji,
//                fontWeight = FontWeight.Bold,
//                fontSize = 20.sp
//            )
                MediumText(
                    modifier = rowItemMod,
                    text = "$operator$currency$cost",
                    color = if (isExpense) MaterialTheme.colors.background else greenHome,
                    fontWeight = FontWeight.Bold
                )

                MediumText(
                    modifier = rowItemMod,
                    text = if (type.isEmpty()) "" else " $connectorText $type",
                    color = MaterialTheme.colors.onSecondary
                )
            }
            if (note.isNotEmpty()) {
                MediumText(
                    modifier = rowModifier.padding(start = 6.dp),
                    text = note,
                    color = MaterialTheme.colors.onSecondary.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                )
            }
            if (drawSep) {
                Spacer(
                    modifier = rowModifier
                        .background(Color.Gray.copy(alpha = 0.4f))
                        .fillMaxWidth()
                        .height(1.dp)
                )
            }
        }
    }


    private fun redirectToProfile() {
        view?.findNavController()?.navigate(R.id.home_to_profile)
    }

    private fun redirectToTransactionList() {
        view?.findNavController()?.navigate(R.id.home_to_tl)
    }
}