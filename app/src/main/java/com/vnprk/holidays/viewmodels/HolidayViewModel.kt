package com.vnprk.holidays.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.vnprk.holidays.App
import com.vnprk.holidays.Repository
import javax.inject.Inject

class HolidayViewModel (application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var repository : Repository

    init{
        getApplication<App>().appComponent.inject(this)
    }

    fun getHoliday(id:Int) = repository.getHolidayById(id)
}
