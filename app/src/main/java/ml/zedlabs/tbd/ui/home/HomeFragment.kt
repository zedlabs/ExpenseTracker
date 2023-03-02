package ml.zedlabs.tbd.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import ml.zedlabs.tbd.MainViewModel
import ml.zedlabs.tbd.R
import ml.zedlabs.tbd.model.Resource
import ml.zedlabs.tbd.model.common.TransactionType
import ml.zedlabs.tbd.ui.common.DefaultTopButton
import ml.zedlabs.tbd.ui.common.HSpacer12
import ml.zedlabs.tbd.ui.common.LargeText
import ml.zedlabs.tbd.ui.common.MediumText
import ml.zedlabs.tbd.ui.common.Spacer12
import ml.zedlabs.tbd.ui.common.Spacer24
import ml.zedlabs.tbd.ui.onboarding.OnboardingViewModel
import ml.zedlabs.tbd.ui.theme.AppThemeType
import ml.zedlabs.tbd.ui.theme.ExpenseTheme
import ml.zedlabs.tbd.ui.theme.greenHome
import ml.zedlabs.tbd.ui.theme.redHome
import ml.zedlabs.tbd.ui.transaction.TransactionViewModel

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
    fun HomeTopSection() {
        val currency by onboardingViewModel.localCurrency.collectAsState()
        val largestTransactionPastWeek = transactionViewModel.largestExpensePastWeek.value
        Column(
            modifier = Modifier
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
                MediumText(
                    text = "Your largest expense was of ${currency.data}${largestTransactionPastWeek?.amount} on ${largestTransactionPastWeek?.type}",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                LastTenDaysSpendChart(lastTenDayExpenses = transactionViewModel.lastTenDayTransactionPairs.value)
                Spacer12()
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
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
                        modifier = Modifier
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
