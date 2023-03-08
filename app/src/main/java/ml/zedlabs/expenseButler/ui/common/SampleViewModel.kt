package ml.zedlabs.expenseButler.ui.common

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ml.zedlabs.expenseButler.repository.AppCommonsRepository
import javax.inject.Inject

// Sample View Model
@HiltViewModel
class SampleViewModel @Inject constructor(
    private val repository: AppCommonsRepository,

) : ViewModel() {

//GET FROM ROOM
//    fun userAddedListState(
//        mediaType: MediaType?,
//        watchStatus: WatchStatus?
//    ): Flow<List<AddedList>> = repository.getUserAddedList(mediaType, watchStatus)

//    private val _topRatedMovieListState =
//        MutableStateFlow<Resource<MovieListResponse>>(Resource.Uninitialised())
//    val topRatedMovieListState = _topRatedMovieListState.asStateFlow()
//
//
//    fun getMovieList() {
//        _topRatedMovieListState.value = Resource.Loading()
//        viewModelScope.launch {
//            _topRatedMovieListState.value =
//                repository.getMovieList(listType = listType.query, page = page)
//        }
//    }
}