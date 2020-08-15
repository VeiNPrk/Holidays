package com.vnprk.holidays.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.vnprk.holidays.App
import com.vnprk.holidays.Repository
import com.vnprk.holidays.models.AdapterItem
import com.vnprk.holidays.models.Event
import com.vnprk.holidays.utils.DatePickerDlg
import com.vnprk.holidays.utils.DateUtils
import com.vnprk.holidays.utils.EventsRecyclerAdapter
import java.util.*
import javax.inject.Inject

class CelebrationListViewModel(application: Application) : AndroidViewModel(application),MyViewModel {

    @Inject lateinit var repository : Repository
    //private val allEventsLiveData = MediatorLiveData<List<AdapterItem<Event>>>()

    init{
        getApplication<App>().appComponent.inject(this)
    }

    /*private val _text = MutableLiveData<String>().apply {
        value = "This is LIST Fragment"
    }
    val text: LiveData<String> = _text*/

    fun getEventByType(type:Int): LiveData<List<AdapterItem<Event>>> {
        return Transformations.map(Transformations.switchMap(repository.getNowDateLong()) {
                dateMils ->repository.getHolidaysByType(
            type,
            DateUtils.getDayFromMills(dateMils),
            DateUtils.getMonthFromMills(dateMils),
            DateUtils.getDayOfYearFromMills(dateMils))
        }) {
            val list: MutableList<AdapterItem<Event>> = mutableListOf()
            it?.let {
                it.forEach { holiday ->
                    list.add(
                        AdapterItem(
                            holiday,
                            EventsRecyclerAdapter.TYPE_ITEM,
                            holiday.type,
                            "",
                            false
                        )
                    )
                }
            }
            list
        }
    }
    fun getLiveDataNowDate() = repository.getNowDateLong()

    fun setNowDate(){
        repository.setNowDate(Calendar.getInstance().timeInMillis)
    }

    override fun onRefreshLayout() {
        setNowDate()
    }
}