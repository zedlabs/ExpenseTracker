package ml.zedlabs.tbd.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ml.zedlabs.tbd.MainViewModel
import ml.zedlabs.tbd.R
import ml.zedlabs.tbd.model.common.CurrencyItem
import ml.zedlabs.tbd.ui.common.MediumText
import ml.zedlabs.tbd.ui.theme.AppThemeType
import ml.zedlabs.tbd.ui.theme.ExpenseTheme

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val onboardingViewModel: OnboardingViewModel by viewModels()
    private val onboardingRowModifier = Modifier
        .fillMaxWidth()

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
                    Onboarding()
                }
            }
        }
    }

    @Composable
    fun Onboarding() {
        val countryData = onboardingViewModel.countryList
        Column() {
            LazyColumn(
                Modifier.weight(1f)
            ) {
                item {
                    OnboardingHeader()
                }
                items(countryData) {
                    CountryListItem(it)
                }
            }
            Button(
                onClick = { view?.findNavController()?.navigate(R.id.onb_to_home) },
                Modifier.align(CenterHorizontally)
            ) {
                MediumText(text = "Continue!")
            }
        }
    }

    @Composable
    fun OnboardingHeader() {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            MediumText(text = "Welcome! Please select your currency before you continue")
        }
    }

    @Composable
    fun CountryListItem(item: CurrencyItem) {
        Row(
            modifier = onboardingRowModifier
                .clickable {
                    onboardingViewModel.countrySelected(item)
                }
                .padding(12.dp)
        ) {
            MediumText(text = item.countryName + "  " + item.flag)
            if (item.countryCode == onboardingViewModel.selectedCountryCodeState.value) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "check")
            }
        }
    }
}