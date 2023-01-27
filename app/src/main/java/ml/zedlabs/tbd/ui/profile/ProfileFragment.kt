package ml.zedlabs.tbd.ui.profile

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
import androidx.compose.material.Switch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import ml.zedlabs.tbd.MainViewModel
import ml.zedlabs.tbd.base.BaseAndroidFragment
import ml.zedlabs.tbd.ui.common.PrimaryText
import ml.zedlabs.tbd.ui.common.Spacer12
import ml.zedlabs.tbd.ui.common.Spacer24
import ml.zedlabs.tbd.ui.common.TitleTextH2
import ml.zedlabs.tbd.ui.theme.AppThemeType
import ml.zedlabs.tbd.ui.theme.ExpenseTheme

@AndroidEntryPoint
class ProfileFragment : BaseAndroidFragment() {

    private val profileViewModel: ProfileViewModel by activityViewModels()
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
                    ProfileScreenParentLayout()
                }
            }
        }
    }

    private fun toggleTheme() {
        profileViewModel.updateTheme()
    }

    @Composable
    fun ProfileScreenParentLayout() {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
                .padding(horizontal = 24.dp)
        ) {
            Spacer24()
            TitleTextH2(text = "App Settings ⚙", modifier = Modifier)
            Spacer24()
            if (profileViewModel.subState.collectAsState(initial = false).value) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PrimaryText(
                        modifier = Modifier.padding(top = 10.dp),
                        text = "Dark Mode (Experimental)"
                    )
                    Switch(
                        checked = profileViewModel.themeState.collectAsState(initial = false).value,
                        onCheckedChange = {
                            toggleTheme()
                        }
                    )
                }
                Spacer12()
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        redirectToPremiumMspScreen()
                    },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PrimaryText(
                    text = "Get Premium!\nDiscover new and exciting features!"
                )
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = "right icons",
                    Modifier.padding(top = 5.dp),
                    tint = MaterialTheme.colors.onBackground
                )
            }
        }

    }

    private fun redirectToPremiumMspScreen() {
        //   view?.findNavController()?.navigate(R.id.profile_to_premium)
    }
}
























