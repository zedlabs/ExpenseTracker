package ml.zedlabs.expenseButler

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ml.zedlabs.expenseButler.repository.AppCommonsRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appRepository: AppCommonsRepository
) : ViewModel() {

    val appTheme = appRepository.getAppTheme()

    fun updateAppTheme(theme: String) {
        viewModelScope.launch {
            appRepository.updateAppTheme(theme)
        }
    }


}