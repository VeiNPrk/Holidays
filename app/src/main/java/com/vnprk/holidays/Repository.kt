package com.vnprk.holidays

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.work.ListenableWorker
import com.vnprk.holidays.models.LoadingState
import com.vnprk.holidays.models.Status
import com.vnprk.holidays.models.*
import java.lang.Exception
import com.vnprk.holidays.Room.LocalDb
import com.vnprk.holidays.Room.RemoteDb
import com.vnprk.holidays.models.Holiday
import com.vnprk.holidays.utils.AlarmUtils
import com.vnprk.holidays.utils.DateUtils
import com.vnprk.holidays.utils.NetworkUtils.Companion.networkState
import com.vnprk.holidays.utils.SharedPreferencesUtils
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val localDb: LocalDb, private val remoteDb: RemoteDb) {
    private val TAG = "Repository"
    private var testHoliday = MutableLiveData<List<Event>>()
    private val _nowDate = MutableLiveData<Long>()
    val nowDate: LiveData<Long>
        get() = _nowDate

    init{
        _nowDate.value=Calendar.getInstance().timeInMillis
    }

    fun getNowDateLong() = nowDate

    fun setNowDate(nowDate:Long){
        _nowDate.value=nowDate
    }

    suspend fun getHolidayByType(types:List<Int>) = localDb.getHolidayByType(types).groupBy { Triple(it.month, it.day, it.dayOfYear) }.values.toList()
        .map { it.first() }

    fun getAllHolidays(day:Int, month:Int, dayOfYear:Int) = /*Transformations.switchMap(nowDate) {
            address -> */localDb.getAllHolidays(day, month, dayOfYear) //}

    fun getHolidaysByType(type:Int, day:Int, month:Int, dayOfYear:Int) = localDb.getHolidaysByType(type, day, month, dayOfYear)

    fun getHolidayById(id:Int) = localDb.getHolidayById(id)

    fun getHolidayActiveAlarm() = localDb.getHolidayActiveAlarm()

    suspend fun updateAlarmHolidays(holidays: List<Holiday>){
        localDb.updateAlarmHolidays(holidays)
    }

    fun getAllPrivateEvents() = localDb.getAllPrivateEvents()

    fun getPrivateEventById(id:Int) = localDb.getPrivateEventById(id)

    suspend fun syncronizeHolidaysData(context: Context): ListenableWorker.Result {
        Log.d("coroutine", "initData Start")
        networkState.postValue(LoadingState.LOADING)
        val response = remoteDb.getAllHolidays(SharedPreferencesUtils.getVersionDb(context))
        Log.d("coroutine", "remoteDb.getAllHolidays() DONE")
        try {
            if (response.isSuccessful && response.code() == 200) {
                val responseData: ResponseData<Holiday>? =
                    response.body()
                return if (responseData!!.isSucces > 0) {
                    if (responseData!!.isSucces == 1) {
                        responseData.result?.let {
                            localDb.synchronizeHolidays(it)
                            SharedPreferencesUtils.setVersionDb(context, responseData.versionDb)
                            SharedPreferencesUtils.setDateUpdateDb(
                                context,
                                responseData.dateUpdateDb
                            )
                            val listTypes = mutableListOf<Int>()
                            SharedPreferencesUtils.getHolidayTipes(context)?.forEach {
                                listTypes.add(it.toInt())
                            }
                            val listHoliday = getHolidayByType(listTypes)
                            listHoliday.forEach { holiday ->
                                holiday.isAlarm = true
                            }
                            AlarmUtils.updateHolidayAlarm(
                                context,
                                getHolidayActiveAlarm(),
                                listHoliday,
                                SharedPreferencesUtils.getNotifyHolidayTime(context)
                            )
                            updateAlarmHolidays(listHoliday)
                            //SharedPreferencesUtils.setHasNewVersionDb(context, false)
                        }
                    }
                    Log.d("coroutine", "localDb.insertInit DONE")
                    networkState.postValue(LoadingState(Status.SUCCESS, ""))
                    ListenableWorker.Result.success()
                } else {
                    networkState.postValue(LoadingState(Status.FAILED, responseData.message))
                    ListenableWorker.Result.retry()
                }
            } else {
                Log.e(TAG, response.message())
                networkState.postValue(LoadingState(Status.FAILED, response.message()))
                return ListenableWorker.Result.retry()
            }
        } catch (e: Exception) {
            val errorMessage = e.message
            Log.d(TAG, errorMessage)
            networkState.postValue(LoadingState(Status.FAILED, errorMessage!!))
            return ListenableWorker.Result.failure()
        } catch (e: Throwable) {
            val errorMessage = e.message
            Log.d(TAG, errorMessage)
            networkState.postValue(LoadingState(Status.FAILED, errorMessage!!))
            return ListenableWorker.Result.failure()
        }
    }

    suspend fun saveEvent(event: PrivateEvent) = localDb.savePrivateEvent(event)

    suspend fun deleteEvent(event: PrivateEvent) { localDb.deletePrivateEvent(event) }
}