package ml.zedlabs.tbd.ui.profile

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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ml.zedlabs.tbd.MainActivity
import ml.zedlabs.tbd.MainActivity.Companion.BillingAction
import ml.zedlabs.tbd.MainViewModel
import ml.zedlabs.tbd.ui.common.PrimaryText
import ml.zedlabs.tbd.ui.common.Spacer12
import ml.zedlabs.tbd.ui.common.Spacer24
import ml.zedlabs.tbd.ui.common.TitleTextH2
import ml.zedlabs.tbd.ui.theme.AppThemeType
import ml.zedlabs.tbd.ui.theme.RedTheme

@AndroidEntryPoint
class PremiumFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                RedTheme(
                    appTheme = mainViewModel.appTheme.collectAsState(initial = AppThemeType.Default.name).value
                ) {
                    PremiumScreenMainLayout()
                }
            }
        }
    }

    @Composable
    fun PremiumScreenMainLayout() {
        if (profileViewModel.subState.collectAsState(initial = false).value) {
            ExistingPremiumUserScreen()
        } else {
            PremiumMSPScreen()
        }
    }

    @Composable
    fun ExistingPremiumUserScreen() {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            TopBar(isPremium = true)
            Spacer24()
            TitleTextH2(text = "🚀🚀🚀\nPremium Feature are Active: ", modifier = Modifier)
            Spacer24()
            PremiumFeatureList()
        }
    }

    fun updateAppTheme(theme: AppThemeType) {
        mainViewModel.updateAppTheme(theme.name)
    }
    @Composable
    fun PremiumMSPScreen() {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .padding(horizontal = 20.dp)
        ) {
            TopBar(isPremium = false)
            Spacer24()
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Theme1", modifier = Modifier.clickable { updateAppTheme(AppThemeType.Default) })
                Text(text = "Theme2", modifier = Modifier.clickable { updateAppTheme(AppThemeType.LightAlternate) })
                Text(text = "Theme3", modifier = Modifier.clickable { updateAppTheme(AppThemeType.Dark) })
            }
            TitleTextH2(
                text = "Activate Premium for just $5 and get access to:",
                modifier = Modifier
            )
            Spacer24()
            PremiumFeatureList()
            Spacer24()
            Spacer24()
            Spacer24()
//            SecondaryActionIcon(
//                modifier = Modifier.align(CenterHorizontally),
//                text = "Get Premium!",
//                vector = painterResource(id = R.drawable.rocket_launch),
//                clickAction = ::subscribeToPremium
//            )
            Spacer24()
            PrimaryText(text = "Restore Purchases", modifier = Modifier
                .align(CenterHorizontally)
                .clickable {
                    checkExistingSub()
                })
        }
    }

    @Composable
    fun PremiumFeatureList() {
        Column {
            PrimaryText(text = "- 📊 More Charts and usage statics ")
            Spacer12()
            PrimaryText(text = "- 🧼 Clean dark mode UI ")
            Spacer12()
            PrimaryText(text = "- First Access to new features and developments and more premium features in the future ")
            Spacer12()
            PrimaryText(text = "- [Upcoming] Import your media and watchtime date.")
        }
    }

    @Composable
    fun TopBar(isPremium: Boolean) {
        Spacer12()
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(35.dp)
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable {
                        view
                            ?.findNavController()
                            ?.navigateUp()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "back-button",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.width(60.dp))
            PrimaryText(
                text = if (isPremium) "Discover Premium!" else "Get Premium!",
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.Center),
                fontWeight = FontWeight.Bold
            )
        }
    }

    private fun subscribeToPremium() {
        (activity as? MainActivity)?.createBillingClient(BillingAction.SHOW_PRODUCTS)
    }

    private fun checkExistingSub() {
        lifecycleScope.launch {
            (activity as? MainActivity)?.createBillingClient(BillingAction.CHECK_ACTIVE_SUB)
        }
    }
}