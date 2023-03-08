package ml.zedlabs.expenseButler.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ml.zedlabs.expenseButler.repository.AppCommonsRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val repository: AppCommonsRepository
) : ViewModel() {

    val themeState = repository.getTheme()
    val subState = repository.getSubState()

    fun updateTheme() {
        viewModelScope.launch {
            repository.updateTheme()
        }
    }

    fun setSubState(active: Boolean) {
        viewModelScope.launch {
            repository.setSubState(active)
        }
    }


    // profile will not have any network interaction, but it will have
    // interaction with shared-prefs and possibly with the local db


}