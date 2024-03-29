package ml.zedlabs.expenseButler.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ml.zedlabs.expenseButler.MainViewModel
import ml.zedlabs.expenseButler.R
import ml.zedlabs.expenseButler.base.BaseAndroidFragment
import ml.zedlabs.expenseButler.ui.common.DefaultTopButton
import ml.zedlabs.expenseButler.ui.common.PrimaryText
import ml.zedlabs.expenseButler.ui.common.Spacer24
import ml.zedlabs.expenseButler.ui.common.TitleTextH2
import ml.zedlabs.expenseButler.ui.theme.AppThemeType
import ml.zedlabs.expenseButler.ui.theme.ExpenseTheme
import ml.zedlabs.expenseButler.util.asApplication

@AndroidEntryPoint
class ProfileFragment : BaseAndroidFragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

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
                    ProfileScreenParentLayout()
                }
            }
        }
    }

    @Composable
    fun ProfileScreenParentLayout() {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
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
            TitleTextH2(text = "App Settings ⚙", modifier = Modifier.align(CenterHorizontally))
            Spacer24()
            Spacer24()
//            if (profileViewModel.subState.collectAsState(initial = false).value) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    PrimaryText(
//                        modifier = Modifier.padding(top = 10.dp),
//                        text = "Dark Mode (Experimental)"
//                    )
//                    Switch(
//                        checked = profileViewModel.themeState.collectAsState(initial = false).value,
//                        onCheckedChange = {
//                            toggleTheme()
//                        }
//                    )
//                }
//                Spacer12()
//            }

            ProfileItemRow(
                clickAction = ::redirectToThemeSelector,
                text = "Change App Theme"
            )
            Spacer24()
            //Not offering right now, maybe later
//            ProfileItemRow(
//                clickAction = ::redirectToPremiumMspScreen,
//                text = "Get Premium!\nDiscover new and exciting features!"
//            )
//            Spacer24()
            ProfileItemRow(
                clickAction = ::redirectToOnboarding,
                text = "Change Currency"
            )
        }

    }

    @Composable
    fun ProfileItemRow(
        clickAction: () -> Unit,
        text: String,
        icon: ImageVector = Icons.Default.KeyboardArrowRight,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    clickAction.invoke()
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PrimaryText(
                text = text
            )
            Icon(
                icon,
                contentDescription = "right icons",
                Modifier.padding(top = 5.dp),
                tint = MaterialTheme.colors.primary
            )
        }
    }

    private fun redirectToPremiumMspScreen() {
        view?.findNavController()?.navigate(R.id.profile_to_premium)
    }

    private fun redirectToOnboarding() {
        activity?.application
            ?.asApplication()
            ?.logFirebase("LANGUAGE_SELECTION_SCREEN")
        val action = ProfileFragmentDirections.profileToOnboarding(true)
        view?.findNavController()?.navigate(action)
    }

    private fun redirectToThemeSelector() {
        view?.findNavController()?.navigate(R.id.profile_to_theme)
    }
}
























