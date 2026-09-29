package otus.homework.flowcats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsRepository: CatsRepository
) : ViewModel() {

    private val _catsStateFlow = MutableStateFlow(CatFactsState())
    val catsStateFlow: StateFlow<CatFactsState> = _catsStateFlow

    init {
        viewModelScope.launch(Dispatchers.IO) {
            catsRepository.listenForCatFacts().collect { result ->
                _catsStateFlow.value = when (result) {
                    is Result.Success -> {
                        _catsStateFlow.value.copy(fact = result.value, error = null)
                    }

                    is Result.Error -> _catsStateFlow.value.copy(error = result.error)
                }
            }
        }
    }
}

data class CatFactsState(
    val fact: Fact? = null,
    val error: Exception? = null,
)

class CatsViewModelFactory(private val catsRepository: CatsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CatsViewModel(catsRepository) as T
}