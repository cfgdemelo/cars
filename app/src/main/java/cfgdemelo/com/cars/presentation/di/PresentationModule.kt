package cfgdemelo.com.cars.presentation.di

import cfgdemelo.com.cars.presentation.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { MainViewModel(get(), get()) }
}