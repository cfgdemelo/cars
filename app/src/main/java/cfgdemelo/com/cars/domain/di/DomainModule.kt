package cfgdemelo.com.cars.domain.di

import cfgdemelo.com.cars.domain.main.MainRepository
import org.koin.dsl.module

val domainModule = module {
    single { MainRepository(get()) }
}