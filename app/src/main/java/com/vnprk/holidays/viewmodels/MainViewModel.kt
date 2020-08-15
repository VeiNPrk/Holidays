package com.vnprk.holidays.viewmodels

import android.app.Application
import androidx.lifecycle.*
import androidx.work.*
import com.vnprk.holidays.models.LoadingState
import com.vnprk.holidays.models.Status
import com.vnprk.holidays.App
import com.vnprk.holidays.Repository
import com.vnprk.holidays.models.*
import com.vnprk.holidays.utils.DateUtils
import com.vnprk.holidays.utils.EventsRecyclerAdapter
import com.vnprk.holidays.utils.NetworkUtils
import com.vnprk.holidays.utils.SharedPreferencesUtils
import com.vnprk.holidays.views.MainFragmentDirections
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel(application: Application) : AndroidViewModel(application), MyViewModel {

    @Inject
    lateinit var repository : Repository
    private val workManager = WorkManager.getInstance(getApplication())
    private val WORK_SAVE_NAME = "work_save_name"
    private val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
    private val allEventsLiveData = MediatorLiveData<List<AdapterItem<Event>>>()
    private var holidaysLiveData : LiveData<List<Holiday>>
    private var privateEventLiveData : LiveData<List<PrivateEvent>>
    init{
        getApplication<App>().appComponent.inject(this)
        workManager.pruneWork()
        holidaysLiveData = Transformations.switchMap(repository.getNowDateLong()) {
                dateMils ->
            repository.getAllHolidays(DateUtils.getDayFromMills(dateMils),
                DateUtils.getMonthFromMills(dateMils),
                DateUtils.getDayOfYearFromMills(dateMils)) }
        privateEventLiveData = repository.getAllPrivateEvents()
        allEventsLiveData.addSource(holidaysLiveData) {
            it?.let {
                allEventsLiveData.value = combineEventsData(holidaysLiveData, privateEventLiveData)
            }
        }
        allEventsLiveData.addSource(privateEventLiveData) {
            it?.let{
                allEventsLiveData.value = combineEventsData(holidaysLiveData, privateEventLiveData)
            }
        }
    }
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun refreshData(){
        if(SharedPreferencesUtils.getVersionDb(getApplication())==0){
            if(!NetworkUtils.isNetworkAvailable(getApplication()))
                NetworkUtils.networkState.postValue(LoadingState(Status.FAILED, NetworkUtils.ERROR_NO_CONNECTION))

            initHolidayData()
        }
    }

    private fun initHolidayData(){
        val simpleRequest = OneTimeWorkRequestBuilder<HolidayWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 2, TimeUnit.SECONDS)
            .build()
        workManager.enqueueUniqueWork(WORK_SAVE_NAME, ExistingWorkPolicy.KEEP, simpleRequest)
    }

    fun getAllEvents() = allEventsLiveData

    private fun combineEventsData(
        holidayEvents: LiveData<List<Holiday>>,
        privateEvents: LiveData<List<PrivateEvent>>
    ): List<AdapterItem<Event>> {
        val list : MutableList<AdapterItem<Event>> = mutableListOf()
        val holidaysList = holidayEvents.value
        val eventsList = privateEvents.value
        if (!holidaysList.isNullOrEmpty()) {
            holidaysList.map { it.type }.distinctBy { it }.sortedBy { it }.forEach{ t->
                list.add(AdapterItem(null, EventsRecyclerAdapter.TYPE_HEADER, t,Event.HOLIDAY_TYPES.get(t)?:"", true))
                list.add(AdapterItem(
                    holidaysList.first { ev -> ev.type == t },
                    EventsRecyclerAdapter.TYPE_ITEM,
                    t,
                    "",
                    holidaysList.filter { ev -> ev.type == t }.size > 1))
            }
        }
        if (!eventsList.isNullOrEmpty()) {
            list.add(AdapterItem(null, EventsRecyclerAdapter.TYPE_HEADER, eventsList[0].type,"Личное", true))
            list.add(AdapterItem(eventsList[0], EventsRecyclerAdapter.TYPE_ITEM, eventsList[0].type, "", eventsList.size > 1))
        }
        return list
    }

    fun getNavigationAction(type:Int) = when(type){
        Event.HOLIDAY_TYPE_CEL-> MainFragmentDirections.actionNavHomeToNavCelebration()
        Event.HOLIDAY_TYPE_COUNTRY-> MainFragmentDirections.actionNavHomeToNavCelebrationCountry()
        Event.HOLIDAY_TYPE_RELIGIOUS-> MainFragmentDirections.actionNavHomeToNavCelebrationReligious()
        Event.HOLIDAY_TYPE_OTHER-> MainFragmentDirections.actionNavHomeToNavCelebrationOther()
        Event.PRIVATE_TYPE-> MainFragmentDirections.actionNavHomeToNavCelebrationPrivate()
        else -> MainFragmentDirections.actionNavHomeToNavCelebration(type)
    }

    private fun setNowDate(){
        repository.setNowDate(Calendar.getInstance().timeInMillis)
    }

    override fun onRefreshLayout() {
        setNowDate()
    }
}