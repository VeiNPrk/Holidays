package com.vnprk.holidays.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.fmklab.fmkinp.NetworkState
import com.fmklab.fmkinp.models.Status
import com.vnprk.holidays.App
import com.vnprk.holidays.Repository
import com.vnprk.holidays.models.Event
import com.vnprk.holidays.models.HolidayWorker
import com.vnprk.holidays.utils.NetworkUtils
import com.vnprk.holidays.utils.SharedPreferencesUtils
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var repository : Repository
    private val workManager = WorkManager.getInstance(getApplication())
    private val WORK_SAVE_NAME = "work_save_name"
    private val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

    init{
        getApplication<App>().appComponent.inject(this)
        workManager.pruneWork()
    }
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun refreshData(){
        if(SharedPreferencesUtils.getVersionDb(getApplication())==0){
            if(!NetworkUtils.isNetworkAvailable(getApplication()))
                NetworkUtils.networkState.postValue(NetworkState(Status.FAILED, NetworkUtils.ERROR_NO_CONNECTION))

            initHolidayData()
            /*else{
                NetworkUtils.networkState.postValue(NetworkState(Status.FAILED, NetworkUtils.ERROR_NO_CONNECTION))
            }*/
        } /*else{

        }*/
        //repository.initTestHoliday()
    }

    private fun initHolidayData(){
        val simpleRequest = OneTimeWorkRequestBuilder<HolidayWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 2, TimeUnit.SECONDS)
            //.setInputData(userData)
            .build()
        workManager.enqueueUniqueWork(WORK_SAVE_NAME, ExistingWorkPolicy.KEEP, simpleRequest)
    }

    fun getTestHoliday() = repository.getAllHolidays()
}