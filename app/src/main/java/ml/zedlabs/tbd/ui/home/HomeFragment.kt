package ml.zedlabs.tbd.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import ml.zedlabs.tbd.MainViewModel
import ml.zedlabs.tbd.R
import ml.zedlabs.tbd.ui.common.DefaultTopButton
import ml.zedlabs.tbd.ui.common.LargeText
import ml.zedlabs.tbd.ui.common.MediumText
import ml.zedlabs.tbd.ui.common.Spacer12
import ml.zedlabs.tbd.ui.common.Spacer24
import ml.zedlabs.tbd.ui.theme.AppThemeType
import ml.zedlabs.tbd.ui.theme.RedTheme

class HomeFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

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
                    Home()
                }
            }
        }
    }

    @Composable
    fun Home() {
        Column {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .fillMaxHeight(.65f)
            ) {
                HomeTopSection()
            }
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.secondary)
                    .fillMaxHeight()
            ) {
                HomeBottomSection()
            }
        }
    }

    @Composable
    fun HomeBottomSection() {
        Column(modifier = Modifier.padding(horizontal = 12.dp)) {
            LargeText(
                modifier = Modifier.padding(vertical = 30.dp),
                text = "Have any purpose",
                fontSize = 64.sp,
                color = MaterialTheme.colors.onSecondary
            )
        }
    }

    @Composable
    fun HomeTopSection() {
        Column(modifier = Modifier.padding(horizontal = 12.dp)) {
            Spacer24()
            DefaultTopButton(imageVector = Icons.Rounded.Person) {
                redirectToProfile()
            }
            LargeText(text = "          The Chart tracks your spending over the past month")
            Spacer12()
            MediumText(text = "Your largest spend was of $43 on Groceries. ")
        }
    }

    fun redirectToProfile() {
        view?.findNavController()?.navigate(R.id.home_to_profile)
    }
}
