package ml.zedlabs.expenseButler.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ml.zedlabs.expenseButler.MainActivity
import ml.zedlabs.expenseButler.MainViewModel
import ml.zedlabs.expenseButler.ui.common.DefaultTopButton
import ml.zedlabs.expenseButler.ui.common.MediumText
import ml.zedlabs.expenseButler.ui.common.Spacer24
import ml.zedlabs.expenseButler.ui.theme.AppThemeType
import ml.zedlabs.expenseButler.ui.theme.ExpenseTheme
import ml.zedlabs.expenseButler.ui.theme.RedBF
import ml.zedlabs.expenseButler.ui.theme.WhiteCC
import ml.zedlabs.expenseButler.ui.theme.altOneBg
import ml.zedlabs.expenseButler.ui.theme.altOneText
import ml.zedlabs.expenseButler.ui.theme.darkBg
import ml.zedlabs.expenseButler.ui.theme.darkText
import ml.zedlabs.expenseButler.util.changeStatusBarColor

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
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
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
        (activity as? MainActivity?)?.changeStatusBarColor(
            MaterialTheme.colors.background.toArgb(),
            false
        )
        Column(
            modifier = mod
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
                .padding(20.dp)
        ) {
            DefaultTopButton(
                imageVector = Icons.Rounded.ArrowBack,
                bgColor = MaterialTheme.colors.onSecondary,
                itemColor = MaterialTheme.colors.onPrimary
            ) {
                view?.findNavController()?.navigateUp()
            }
            Spacer24()
            Spacer24()
            ThemeItem(
                text = "Default Colors",
                type = AppThemeType.Default,
                color1 = RedBF,
                color2 = WhiteCC
            )
            ThemeItem(
                text = "Alternate Colors",
                type = AppThemeType.Dark,
                color1 = darkBg,
                color2 = darkText
            )
            ThemeItem(
                text = "Alternate Colors #2",
                type = AppThemeType.LightAlternate,
                color1 = altOneBg,
                color2 = altOneText
            )
        }

    }

    @Composable
    fun ThemeItem(
        text: String,
        type: AppThemeType,
        mod: Modifier = Modifier,
        color1: Color,
        color2: Color
    ) {
        val theme = mainViewModel.appTheme.collectAsState(initial = AppThemeType.Default.name).value
        Row(
            modifier = mod
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .clickable { updateAppTheme(type) }
                .border(
                    width = 2.dp,
                    color = if (type.name == theme) MaterialTheme.colors.primary else MaterialTheme.colors.background
                )
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MediumText(
                text = text,
                color = MaterialTheme.colors.primary,
                modifier = mod.align(CenterVertically)
            )
            Column(modifier = mod.padding(end = 20.dp)) {
                Box(
                    modifier = mod
                        .width(60.dp)
                        .height(40.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                        .background(color = color1)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colors.primary,
                            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                        )
                )
                Box(
                    modifier = mod
                        .width(60.dp)
                        .height(40.dp)
                        .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                        .background(color = color2)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colors.primary,
                            shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                        )
                )
            }
        }
    }
}