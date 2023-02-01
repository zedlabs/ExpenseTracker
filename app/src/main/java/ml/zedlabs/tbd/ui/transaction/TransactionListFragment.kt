package ml.zedlabs.tbd.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
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
    fun TransactionListParent() {
        BottomSheetScaffold(sheetContent = {
            AddTransactionBottomSheet()
        }) {
            Column(
                modifier = Modifier
                    .background(color = MaterialTheme.colors.secondary)
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
            ) {
                Spacer24()
                LargeText(
                    text = "Welcome to transaction list",
                    color = MaterialTheme.colors.onSecondary
                )
                Spacer24()
                MediumText(text = "Add Expense / Income",
                    modifier = Modifier.clickable {
                        // open the bottom sheet
                    }
                )
            }
        }
    }

    @Composable
    private fun AddTransactionBottomSheet() {

    }


}