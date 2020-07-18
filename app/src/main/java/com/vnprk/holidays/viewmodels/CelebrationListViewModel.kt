package com.vnprk.holidays.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.vnprk.holidays.App
import com.vnprk.holidays.Repository
import javax.inject.Inject

class CelebrationListViewModel(application: Application) : AndroidViewModel(application) {

    @Inject lateinit var repository : Repository

    init{
        getApplication<App>().appComponent.inject(this)
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is LIST Fragment"
    }
    val text: LiveData<String> = _text
}