package com.vnprk.holidays.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.vnprk.holidays.App
import com.vnprk.holidays.Repository
import com.vnprk.holidays.models.AdapterItem
import com.vnprk.holidays.models.Event
import com.vnprk.holidays.utils.EventsRecyclerAdapter
import java.util.*
import javax.inject.Inject

class PrivateListViewModel(application: Application) : AndroidViewModel(application),MyViewModel {

    @Inject
    lateinit var repository : Repository
    init{
        getApplication<App>().appComponent.inject(this)
    }

    fun getPrivateEvents(): LiveData<List<AdapterItem<Event>>> {
        return Transformations.map(repository.getAllPrivateEvents()) {
            val list : MutableList<AdapterItem<Event>> = mutableListOf()
            it?.let{
                it.forEach{event->
                    list.add(AdapterItem(event, EventsRecyclerAdapter.TYPE_ITEM, event.type, "", false))
                }
            }
            list
        }
    }

    fun setNowDate(){
        repository.setNowDate(Calendar.getInstance().timeInMillis)
    }

    override fun onRefreshLayout() {
        setNowDate()
    }
}