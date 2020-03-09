package cfgdemelo.com.cars

import android.app.Application
import cfgdemelo.com.cars.data.di.dataModule
import cfgdemelo.com.cars.domain.di.domainModule
import cfgdemelo.com.cars.presentation.di.presentationModule
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CarsApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)

        startKoin {
            androidLogger()
            androidContext(this@CarsApp)
            modules(listOf(dataModule, domainModule, presentationModule))
        }
    }
}