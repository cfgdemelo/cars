package cfgdemelo.com.cars.domain.main

import cfgdemelo.com.cars.data.model.Car
import cfgdemelo.com.cars.data.remote.api.CarsApi

class MainRepository(private val api: CarsApi) {
    suspend fun getCarList(): List<Car> = api.getCarList()
}