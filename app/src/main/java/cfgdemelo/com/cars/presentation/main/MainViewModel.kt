package cfgdemelo.com.cars.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cfgdemelo.com.cars.data.model.Car
import cfgdemelo.com.cars.domain.main.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository): ViewModel() {

    private val _cars = MutableLiveData<List<Car>>()
    val cars: LiveData<List<Car>> = _cars

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getCarList(forceUpdate: Boolean) {
        try {
            if (forceUpdate) {
                viewModelScope.launch {
                    _cars.value = repository.getCarList()
                }
            } else {
                _cars.value = cars.value
            }
        } catch (exception: Throwable) {
            exception.printStackTrace()
            _error.value = exception.message
        }
    }

}