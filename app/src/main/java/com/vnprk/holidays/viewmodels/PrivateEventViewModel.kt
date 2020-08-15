package com.vnprk.holidays.viewmodels

import android.app.Activity
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.lifecycle.*
import com.vnprk.holidays.App
import com.vnprk.holidays.R
import com.vnprk.holidays.Repository
import com.vnprk.holidays.models.Event
import com.vnprk.holidays.models.LoadingState
import com.vnprk.holidays.models.PrivateEvent
import com.vnprk.holidays.models.Status
import com.vnprk.holidays.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import petrov.kristiyan.colorpicker.ColorPicker
import petrov.kristiyan.colorpicker.ColorPicker.OnFastChooseColorListener
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class PrivateEventViewModel (application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var repository : Repository
    private val _event = MutableLiveData<PrivateEvent>()
    val VIEW_CODE_START_DATE = 1
    val VIEW_CODE_FINISH_DATE = 2
    val VIEW_CODE_START_TIME = 3
    val VIEW_CODE_FINISH_TIME = 4
    val ACTION_SAVE_COMPLETE = "action_code_save_complete"
    val event: LiveData<PrivateEvent>
        get() = _event
    var privateEvent: PrivateEvent? = null
    var eventId = 0
    val loadStateLiveData = MutableLiveData<LoadingState>()
    private val periodItemPosition = MutableLiveData<Int>(0)
    private var periodKeyValue
    get() =
    periodItemPosition.value?.let {
        Event.periods.keys.toList()[it]
    } ?: 0
    set(value) {
        val position = Event.periods.keys.toList().indexOfFirst {
            it == value
        }
        if (position != -1) {
            periodItemPosition.value = position
        }
    }
    private var _periodNameValue = MutableLiveData<String>(Event.periods.get(0))
    var periodNameValue :LiveData<String> = _periodNameValue

    private val colorItemPosition = MutableLiveData<Int>(0)
    private var colorKeyValue
        get() =
            colorItemPosition.value?.let {
                Event.colors.keys.toList()[it]
            } ?: 0
        set(value) {
            val position = Event.colors.keys.toList().indexOfFirst {
                it == value
            }
            if (position != -1) {
                colorItemPosition.value = position
            }
        }
    private var _colorNameValue = MutableLiveData<String>(Event.colors.get(0))
    var colorNameValue :LiveData<String> = _colorNameValue

    private val notifyItemPosition = MutableLiveData<Int>(0)
    private var notifyKeyValue
        get() =
            notifyItemPosition.value?.let {
                Event.notifys.keys.toList()[it]
            } ?: 0
        set(value) {
            val position = Event.notifys.keys.toList().indexOfFirst {
                it == value
            }
            if (position != -1) {
                notifyItemPosition.value = position
            }
        }
    private var _notifyNameValue = MutableLiveData<String>(Event.notifys.get(0))
    var notifyNameValue :LiveData<String> = _notifyNameValue

    private var _fullDateStrLiveData = MutableLiveData<String>(Event.periods.get(0))
    var fullDateStrLiveData :LiveData<String> = _fullDateStrLiveData

    init{
        getApplication<App>().appComponent.inject(this)
    }

    private fun initNewEvent(): PrivateEvent {
        if (privateEvent == null) {
            privateEvent = PrivateEvent(
                "",
                "",
                null,
                Event.PRIVATE_TYPE,
                DateUtils.getNowDateTimeMills(),
                DateUtils.getNowDateTimeMills(),
                0,
                0,
                0
            )
        }
        return privateEvent as PrivateEvent
    }

    private fun initParametersEvent(){
        periodKeyValue = privateEvent!!.period
        _periodNameValue.value=Event.periods.get(periodKeyValue)
        colorKeyValue = privateEvent!!.idColor
        _colorNameValue.postValue(Event.colors.get(colorKeyValue))
        notifyKeyValue = privateEvent!!.notifyBefore
        _notifyNameValue.postValue(Event.notifys.get(notifyKeyValue))
        _fullDateStrLiveData.postValue(DateUtils.getFullDateFormat(getApplication(), privateEvent!!.startDateTime, privateEvent!!.finishDateTime))
    }

    fun getPrivateEvent(id:Int): LiveData<PrivateEvent> {
        if (eventId != id) {
            eventId = id
            return Transformations.switchMap(repository.getPrivateEventById(id)) {
                it?.let{
                    privateEvent = it as PrivateEvent
                    initParametersEvent()
                    _event.postValue(privateEvent)
                }
                event
            }
        } else{
            _event.value = initNewEvent()
        }
        return event
    }

    fun getPeriodDialog(listener: PeriodsDlg.OnPeriodListener): PeriodsDlg {
        val dlg = PeriodsDlg.newInstance(periodItemPosition.value ?: 0)
        dlg.setListener(listener)
        return dlg
    }

    fun setPeriod(period:Int){
        periodItemPosition.value=period
        _periodNameValue.postValue(Event.periods.get(periodKeyValue))
    }

    fun getNotifyDialog(listener: NotifysDlg.OnNotifyListener): NotifysDlg {
        val dlg = NotifysDlg.newInstance(notifyItemPosition.value ?: 0)
        dlg.setListener(listener)
        return dlg
    }

    fun setNotify(notify:Int){
        notifyItemPosition.value=notify
        _notifyNameValue.postValue(Event.notifys.get(notifyKeyValue))
    }

    fun getColorDialog(activity: Activity)  = ColorPicker(activity)
            .setOnFastChooseColorListener(object : OnFastChooseColorListener {
                override fun setOnFastChooseColorListener(position: Int, color: Int) {
                    colorItemPosition.value=position
                    _colorNameValue.postValue(Event.colors.get(colorKeyValue))
                }
                override fun onCancel() {
                }
            })
            .setRoundColorButton(true)
            .disableDefaultButtons(true)
            .setColors(ArrayList<String>(Event.colors.values))
            .setDefaultColorButton(Color.parseColor("#f84c44"))
            .setColumns(5)

    fun getDateDialog(listener: DatePickerDlg.OnDateCompleteListener, date: Long, viewCode:Int): DatePickerDlg {
        val dlg = DatePickerDlg.newInstance(date, viewCode)
        dlg.setListener(listener)
        return dlg
    }

    fun getTimeDialog(listener: TimePickerDlg.OnTimeCompleteListener, date: Long, viewCode:Int): TimePickerDlg {
        val dlg = TimePickerDlg.newInstance(date, viewCode)
        dlg.setListener(listener)
        return dlg
    }

    fun setDateEvent(date: Calendar, viewCode:Int) {
        if(viewCode==VIEW_CODE_START_DATE) {
            privateEvent?.startDateTime = date.timeInMillis
            if(privateEvent!!.startDateTime > privateEvent!!.finishDateTime){
                privateEvent!!.finishDateTime=privateEvent!!.startDateTime
            }
        }
        else if (viewCode==VIEW_CODE_FINISH_DATE)
            privateEvent?.finishDateTime = date.timeInMillis
        _event.value = privateEvent
    }

    fun setTimeEvent(date: Calendar, viewCode:Int) {
        if(viewCode==VIEW_CODE_START_TIME) {
            privateEvent?.startDateTime = date.timeInMillis
            if(privateEvent!!.startDateTime > privateEvent!!.finishDateTime){
                privateEvent!!.finishDateTime=privateEvent!!.startDateTime
            }
        }
        else if (viewCode==VIEW_CODE_FINISH_TIME)
            privateEvent?.finishDateTime = date.timeInMillis
        _event.value = privateEvent
    }

    //fun getStrFullDate() =

    fun checkValidation():Boolean{
        if(privateEvent!!.finishDateTime<privateEvent!!.startDateTime) return false
        return true
    }

    fun setAlarm(context: Context?){
        context?.let{
            AlarmUtils.setAlarm(it,
                privateEvent!!.name?:context.resources.getString(R.string.tv_event_name_null),
                DateUtils.getMilsWithAdding(privateEvent!!.startDateTime, privateEvent!!.notifyBefore),
                privateEvent!!.id
            )
            /*val alarmManager : AlarmManager = it.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val myIntent = Intent(it, AlarmBroadcastReceiver::class.java)
            myIntent.putExtra(AlarmBroadcastReceiver.Companion.TITLE, privateEvent!!.name?:context.resources.getString(R.string.tv_event_name_null))
            val pendingIntent =
                PendingIntent.getBroadcast(it, 0, myIntent, 0)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    DateUtils.getMilsWithAdding(privateEvent!!.startDateTime, privateEvent!!.notifyBefore),
                pendingIntent)
            else
                alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                    DateUtils.getMilsWithAdding(privateEvent!!.startDateTime, privateEvent!!.notifyBefore),
                pendingIntent
            )*/
        }
    }

    fun saveEvent(){
        privateEvent?.idColor = colorKeyValue
        privateEvent?.period = periodKeyValue
        privateEvent?.notifyBefore = notifyKeyValue
        if(checkValidation()){
            viewModelScope.launch(Dispatchers.IO) {
                loadStateLiveData.postValue(LoadingState.LOADING)
                privateEvent?.let { event->
                    try {
                        repository.saveEvent(event)
                    } catch (ex: Exception) {
                        val errorMessage = ex.message
                        loadStateLiveData.postValue(LoadingState(Status.FAILED, errorMessage!!))
                    }
                }
                loadStateLiveData.postValue(LoadingState(Status.SUCCESS, ACTION_SAVE_COMPLETE))
            }
        } else{
            loadStateLiveData.postValue(LoadingState(Status.FAILED, "Дата и Время окончания должна быть больше или равна Дате и Времени начала"))
        }
    }
}
