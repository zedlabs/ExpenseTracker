package ml.zedlabs.expenseButler.ui.onboarding

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ml.zedlabs.expenseButler.MainActivity
import ml.zedlabs.expenseButler.MainViewModel
import ml.zedlabs.expenseButler.R
import ml.zedlabs.expenseButler.model.Resource
import ml.zedlabs.expenseButler.model.common.CurrencyItem
import ml.zedlabs.expenseButler.ui.common.LargeText
import ml.zedlabs.expenseButler.ui.common.MediumText
import ml.zedlabs.expenseButler.ui.common.SemiLargeText
import ml.zedlabs.expenseButler.ui.common.Spacer12
import ml.zedlabs.expenseButler.ui.theme.AppThemeType
import ml.zedlabs.expenseButler.ui.theme.ExpenseTheme
import ml.zedlabs.expenseButler.util.changeStatusBarColor

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val onboardingViewModel: OnboardingViewModel by activityViewModels()
    private val onboardingRowModifier = Modifier.fillMaxWidth()
    val args: OnboardingFragmentArgs by navArgs()

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
                // need this before loading the onboarding fragment, ideally create a splash fragment
                val currency by onboardingViewModel.localCurrency.collectAsState()
                if (args.isFromProfile) {
                    ShowOnboardingScreen()
                    return@setContent
                }
                when (currency) {
                    is Resource.Error -> {
                        //error implies currency is not set
                        ShowOnboardingScreen()
                    }

                    is Resource.Success -> {
                        navigateToHome()
                    }

                    else -> {
                        // need to wait at this stage
                        Log.e(TAG, "onCreateView: Error on onboarding")
                    }
                }
            }
        }
    }

    @Composable
    private fun ShowOnboardingScreen() {
        ExpenseTheme(
            appTheme = mainViewModel.appTheme.collectAsState(initial = AppThemeType.Default.name).value
        ) {
            Onboarding()
        }
    }

    @Composable
    fun EmptyFullBackground() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        )
    }

    @Composable
    fun Onboarding(modifier: Modifier = Modifier) {
        (activity as? MainActivity?)?.changeStatusBarColor(
            MaterialTheme.colors.background.toArgb(),
            false
        )
        val countryData = onboardingViewModel.countryList
        Column(modifier = modifier.background(MaterialTheme.colors.secondary)) {
            LazyColumn(
                modifier.weight(1f)
            ) {
                item {
                    OnboardingHeader()
                }
                items(countryData) {
                    CountryListItem(it)
                }
            }
            Button(
                onClick = { commitAndNavigateToHome() },
                modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.background,
                    disabledBackgroundColor = MaterialTheme.colors.background.copy(alpha = 0.2f)
                ),
                enabled = onboardingViewModel.selectedCountryCodeState.value != null
            ) {
                MediumText(
                    text = "Continue!",
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Light,
                )
            }
        }
    }

    private fun commitAndNavigateToHome() {
        onboardingViewModel.commitCurrencyToPrefs()
        when (args.isFromProfile) {
            true -> {
                // opened from profile, need to remove the backstack
                val navController = view?.findNavController()
                navController?.popBackStack(R.id.home_fragment, false)
            }

            false -> {
                // initial setup
                navigateToHome()
            }
        }
    }

    private fun navigateToHome() {
        val navController = view?.findNavController()
        val startDestination = navController?.graph?.startDestinationId ?: return
        val navOptions = NavOptions.Builder()
            .setPopUpTo(startDestination, inclusive = true, saveState = false)
            .build()
        navController.navigate(R.id.onb_to_home, null, navOptions)
    }

    @Composable
    fun OnboardingHeader() {
        Column(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
                .padding(12.dp)
        ) {
            LargeText(text = "Welcome !", color = MaterialTheme.colors.primary)
            Spacer12()
            SemiLargeText(
                text = "Please select your currency before you continue",
                color = MaterialTheme.colors.primary
            )
        }
    }

    val colMod = Modifier

    @Composable
    fun CountryListItem(item: CurrencyItem) {
        val selectedItem = onboardingViewModel.selectedCountryCodeState.value

        Column(
            modifier = colMod
                .fillMaxWidth()
                .border(
                    2.dp, if (selectedItem != null
                        && item.countryCode == selectedItem.countryCode
                        && item.currencySymbol == selectedItem.currencySymbol
                    ) MaterialTheme.colors.background else MaterialTheme.colors.secondary
                )
        ) {
            Row(
                modifier = onboardingRowModifier
                    .clickable {
                        onboardingViewModel.countrySelected(item)
                    }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MediumText(
                    text = item.flag + "   " + item.countryName,
                    color = MaterialTheme.colors.onSecondary
                )
                MediumText(
                    text = item.currencySymbol,
                    color = MaterialTheme.colors.onSecondary
                )

            }
        }
    }
}