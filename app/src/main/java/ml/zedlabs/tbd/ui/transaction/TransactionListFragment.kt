package ml.zedlabs.tbd.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ml.zedlabs.tbd.MainViewModel
import ml.zedlabs.tbd.ui.common.LargeText
import ml.zedlabs.tbd.ui.common.MediumText
import ml.zedlabs.tbd.ui.common.Spacer24
import ml.zedlabs.tbd.ui.theme.AppThemeType
import ml.zedlabs.tbd.ui.theme.ExpenseTheme

@OptIn(ExperimentalMaterialApi::class)
@AndroidEntryPoint
class TransactionListFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val transactionViewModel: TransactionViewModel by viewModels()

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

        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = 0.dp,
            sheetContent = {
                AddTransactionBottomSheet()
            },
            modifier = mod.pointerInput(Unit) {
                detectTapGestures(onTap = {
                    scope.launch {
                        addTransactionSheetState.collapse()
                    }
                })
            }
        ) {
            val listItems =
                transactionViewModel.getUsersTransactions().collectAsState(initial = listOf()).value

            LazyColumn(
                modifier = mod
                    .background(color = MaterialTheme.colors.secondary)
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
            ) {
                item {
                    TransactionHeaderItem(
                        scope = scope,
                        addTransactionSheetState = addTransactionSheetState
                    )
                }
                items(items = listItems) { item ->
                    MediumText(text = "${item.type}  ${item.transactionId}  ${item.note}")
                }
            }
        }
    }

    @Composable
    fun TransactionHeaderItem(
        mod: Modifier = Modifier,
        scope: CoroutineScope,
        addTransactionSheetState: BottomSheetState
    ) {
        Spacer24()
        LargeText(
            text = "Welcome to transaction list",
            color = MaterialTheme.colors.onSecondary
        )
        Spacer24()
        MediumText(
            text = "Add Expense / Income",
            modifier = mod.clickable {
                // open the bottom sheet
                scope.launch {
                    addTransactionSheetState.expand()
                }
            },
            color = MaterialTheme.colors.onSecondary
        )
    }

    @Composable
    private fun AddTransactionBottomSheet(mod: Modifier = Modifier) {
        Box(modifier = mod
            .fillMaxWidth()
            .clickable { }) {
            MediumText(text = "Add Fake",
                color = MaterialTheme.colors.onSecondary,
                modifier = mod
                    .clickable {
                        transactionViewModel.addFakeEntryToTransactionDb()
                    }
                    .padding(20.dp)
            )
        }
    }


}