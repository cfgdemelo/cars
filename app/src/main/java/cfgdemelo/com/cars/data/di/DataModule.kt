package cfgdemelo.com.cars.data.di

import cfgdemelo.com.cars.BuildConfig
import cfgdemelo.com.cars.data.remote.api.CarsApi
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val dataModule = module {
    factory { providesOkHttpClient() }
    single {
        createWebService<CarsApi>(
            okHttpClient = get(),
            url = BuildConfig.BASE_URL
        )
    }
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .baseUrl(url)
        .client(okHttpClient)
        .build()
        .create(T::class.java)
}

fun providesOkHttpClient(): OkHttpClient {
    try {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS).apply {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                this.addInterceptor(interceptor)
                this.addNetworkInterceptor(StethoInterceptor())
            }.build()
    } catch (exception: Exception) {
        exception.printStackTrace()
        throw Exception(exception)
    }
}