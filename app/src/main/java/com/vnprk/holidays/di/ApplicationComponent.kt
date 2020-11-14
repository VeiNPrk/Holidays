package com.vnprk.holidays.di

import android.content.Context
import com.vnprk.holidays.App
import com.vnprk.holidays.MainActivity
import com.vnprk.holidays.models.HolidayWorker
import com.vnprk.holidays.utils.AlarmRebootService
import com.vnprk.holidays.utils.AlarmService
//import com.vnprk.holidays.models.MyWorker
import com.vnprk.holidays.viewmodels.*
import com.vnprk.holidays.views.SettingsFragment
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
    fun inject(activity:MainActivity)
    fun inject(viewModel: MainViewModel)
    fun inject(viewModel: CelebrationListViewModel)
    fun inject(viewModel: PrivateListViewModel)
    fun inject(viewModel: HolidayViewModel)
    fun inject(viewModel: PrivateEventViewModel)
    fun inject(alarmService:AlarmService)
    fun inject(alarmService: AlarmRebootService)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(app:App)
    /*fun inject(viewModel: SplashScreenViewModel)
    fun inject(viewModel: RegistrationViewModel)
    fun inject(viewModel: DetailCastViewModel)
    fun inject(viewModel: DetailNotNumViewModel)
    fun inject(viewModel: DetailPairViewModel)
    fun inject(worker: MyWorker)*/
}