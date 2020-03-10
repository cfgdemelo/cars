package cfgdemelo.com.cars.main

import android.location.Location
import cfgdemelo.com.cars.CarsApp
import cfgdemelo.com.cars.data.di.dataModule
import cfgdemelo.com.cars.data.model.Car
import cfgdemelo.com.cars.domain.di.domainModule
import cfgdemelo.com.cars.domain.main.MainRepository
import cfgdemelo.com.cars.presentation.di.presentationModule
import cfgdemelo.com.cars.presentation.main.MainViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.*
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

class MainTest : KoinTest {

    private val mainViewModel: MainViewModel by inject()
    private val mainRepository: MainRepository by inject()

    @Test
    fun locationInserted_isTheSame() {
        startKoin()
        val latitude: Double = -23.542096677319027
        val longitude: Double = -46.658216017167874
        val firstLocation = Location("first")
        firstLocation.latitude = latitude
        firstLocation.longitude = longitude

        val secondLocation = Location("second").apply {
            this.latitude = latitude
            this.longitude = longitude
        }

        assertEquals(
            "Return must be zero",
            "0",
            mainViewModel.getDistance(firstLocation, secondLocation)
        )

        stopKoin()
    }

    @Test
    fun checkCarsList_isNotNullOrEmpty() {
        startKoin()

        runBlocking(Dispatchers.IO) {
            val list: List<Car>? = try {
                mainRepository.getCarList()
            } catch (exception: Throwable) {
                null
            }
            assertEquals("List is not Null or Empty", true, !list.isNullOrEmpty())
        }

        stopKoin()
    }

    private fun startKoin() {
        startKoin {
            androidContext(CarsApp())
            modules(listOf(dataModule, domainModule, presentationModule))
        }
    }
}