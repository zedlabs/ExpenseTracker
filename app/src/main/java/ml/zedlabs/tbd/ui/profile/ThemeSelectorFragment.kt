package ml.zedlabs.tbd.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ml.zedlabs.tbd.MainViewModel
import ml.zedlabs.tbd.ui.theme.AppThemeType
import ml.zedlabs.tbd.ui.theme.ExpenseTheme

@AndroidEntryPoint
class ThemeSelectorFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()

    private fun updateAppTheme(theme: AppThemeType) {
        mainViewModel.updateAppTheme(theme.name)
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
                    MainThemeSelectorLayout()
                }
            }
        }
    }

    @Composable
    fun MainThemeSelectorLayout(mod: Modifier = Modifier) {
        Column(modifier = mod.fillMaxSize()) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = mod.fillMaxWidth()) {
                Text(
                    text = "Theme1",
                    modifier = mod.clickable { updateAppTheme(AppThemeType.Default) })
                Text(
                    text = "Theme2",
                    modifier = mod.clickable { updateAppTheme(AppThemeType.LightAlternate) })
                Text(
                    text = "Theme3",
                    modifier = mod.clickable { updateAppTheme(AppThemeType.Dark) })
            }
        }
    }
}