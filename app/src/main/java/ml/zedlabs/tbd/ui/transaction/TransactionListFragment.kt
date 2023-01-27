package ml.zedlabs.tbd.ui.transaction

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import ml.zedlabs.tbd.MainViewModel
import ml.zedlabs.tbd.ui.common.LargeText
import ml.zedlabs.tbd.ui.theme.AppThemeType
import ml.zedlabs.tbd.ui.theme.ExpenseTheme

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
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colors.secondary)
                .fillMaxSize()
        ) {
            LargeText(text = "Welcome to transaction list", color = MaterialTheme.colors.onSecondary)
        }
    }


}