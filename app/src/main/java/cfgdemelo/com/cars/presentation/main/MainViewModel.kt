package cfgdemelo.com.cars.presentation.main

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cfgdemelo.com.cars.R
import cfgdemelo.com.cars.data.model.Car
import cfgdemelo.com.cars.domain.main.MainRepository
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class MainViewModel(private val context: Context, private val repository: MainRepository) :
    ViewModel() {

    private val _cars = MutableLiveData<List<Car>>()
    val cars: LiveData<List<Car>> = _cars

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getCarList(forceUpdate: Boolean) {
        if (forceUpdate) {
            viewModelScope.launch {
                try {
                    _cars.value = repository.getCarList()
                } catch (exception: Throwable) {
                    exception.printStackTrace()
                    _error.value = exception.message
                }
            }
        } else {
            if (_cars.value != null) {
                _cars.value = cars.value
            } else {
                _error.value = context.getString(R.string.network_error)
            }
        }
    }

    suspend fun getMapData(latLng: LatLng, title: String, location: Location): MarkerOptions =
        withContext(Dispatchers.IO) {
            var distance: String
            Location("My location").apply {
                latitude = latLng.latitude
                longitude = latLng.longitude
                val dist = this.distanceTo(location).roundToInt().div(1000).toString()
                distance = context.getString(R.string.distance, dist)
            }
            MarkerOptions().position(latLng).title(title).snippet(distance)
        }

}