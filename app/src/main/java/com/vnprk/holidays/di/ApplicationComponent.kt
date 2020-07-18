package com.vnprk.holidays.di

import android.content.Context
import com.vnprk.holidays.models.HolidayWorker
//import com.vnprk.holidays.models.MyWorker
import com.vnprk.holidays.viewmodels.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(NetworkModule::class, LocalDbModule::class))
interface ApplicationComponent {
    // This tells Dagger that LoginActivity requests injection so the graph needs to
    // satisfy all the dependencies of the fields that LoginActivity is requesting.
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder
        fun build(): ApplicationComponent
    }

    fun inject(worker: HolidayWorker)
    fun inject(viewModel: MainViewModel)
    fun inject(viewModel: CelebrationListViewModel)
    fun inject(viewModel: PrivateListViewModel)
    /*fun inject(viewModel: SplashScreenViewModel)
    fun inject(viewModel: RegistrationViewModel)
    fun inject(viewModel: DetailCastViewModel)
    fun inject(viewModel: DetailNotNumViewModel)
    fun inject(viewModel: DetailPairViewModel)
    fun inject(worker: MyWorker)*/
}