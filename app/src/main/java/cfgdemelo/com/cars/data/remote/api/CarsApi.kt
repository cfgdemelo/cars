package cfgdemelo.com.cars.data.remote.api

import cfgdemelo.com.cars.data.model.Car
import retrofit2.http.GET

interface CarsApi {
    @GET("$CAR/list/")
    suspend fun getCarList(): List<Car>

    companion object {
        private const val CAR = "/car"
    }
}