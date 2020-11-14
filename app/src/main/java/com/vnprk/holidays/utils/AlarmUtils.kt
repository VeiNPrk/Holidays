package com.vnprk.holidays.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.vnprk.holidays.R
import com.vnprk.holidays.models.Event
import com.vnprk.holidays.models.Holiday
import com.vnprk.holidays.models.PrivateEvent
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmUtils {
    companion object {
        const val ACTION_NOTIFY_CANCEL = "ACTION_NOTIFY_CANCEL"
        const val KEY_ACTION_NOTIFY = "KEY_ACTION_NOTIFY"
        const val FROM_NOTIF = "FROM_NOTIF"
        const val TYPE_EVENT = "TYPE_EVENT"
        const val TITLE = "TITLE"
        const val ID_EVENT = "ID_EVENT"

        fun setAlarm(context: Context, tittle:String, dateTimeMils: Long, eventId: Int, eventType:Int) {
            val alarmManager: AlarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val myIntent = if(eventType == Event.PRIVATE_TYPE) Intent(context, AlarmBroadcastReceiverPrivate::class.java)
                else Intent(context, AlarmBroadcastReceiverHoliday::class.java)
            myIntent.putExtra(ID_EVENT, eventId)
            myIntent.putExtra(TYPE_EVENT, eventType)
            myIntent.putExtra(TITLE, tittle)
            val pendingIntent =
                PendingIntent.getBroadcast(context, eventId, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    dateTimeMils,/*DateUtils.getMilsWithAdding(startDateTimeMils, beforeId),*/
                    pendingIntent
                )
            else
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    dateTimeMils,/*DateUtils.getMilsWithAdding(startDateTimeMils, beforeId),*/
                    pendingIntent
                )
        }

        fun cancelAlarm(context: Context, tittle:String, eventId: Int, eventType:Int) {
            val alarmManager: AlarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val myIntent = if(eventType == Event.PRIVATE_TYPE) Intent(context, AlarmBroadcastReceiverPrivate::class.java)
                else Intent(context, AlarmBroadcastReceiverHoliday::class.java)
            myIntent.putExtra(ID_EVENT, eventId)
            myIntent.putExtra(TYPE_EVENT, eventType)
            myIntent.putExtra(TITLE, tittle)
            val pendingIntent =
                PendingIntent.getBroadcast(context, eventId, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.cancel(pendingIntent)
        }

        fun setAlarmRepeat(
            context: Context,
            isPrivate: Boolean,
            tittle:String,
            event:Event,
            periodSetting:Int,
            isRepeating:Boolean=true
            ){
            if(isPrivate)
                alarmRepeatPrivate(context, tittle, event as PrivateEvent, periodSetting)
            else
                alarmRepeatHoliday(context, tittle, event as Holiday, isRepeating)
        }

        private fun alarmRepeatPrivate(context: Context, tittle:String, event:PrivateEvent, periodSetting:Int) {
            val calendarAlarmTime = Calendar.getInstance()
            val calendarStart = Calendar.getInstance()
            calendarStart.timeInMillis = event.startDateTime
            val calendarFinish = Calendar.getInstance()
            calendarFinish.timeInMillis = event.finishDateTime
            val calendarNow = Calendar.getInstance()
            calendarStart.set(Calendar.YEAR, calendarNow.get(Calendar.YEAR))
            calendarStart.set(Calendar.MONTH, calendarNow.get(Calendar.MONTH))
            calendarStart.set(Calendar.DAY_OF_MONTH, calendarNow.get(Calendar.DAY_OF_MONTH))
            if (event.finishDateTime < calendarStart.timeInMillis && event.period > 0)
                while (calendarFinish.timeInMillis < calendarStart.timeInMillis) {
                    calendarFinish.timeInMillis =
                        DateUtils.getMilsWithAddingPeriod(calendarFinish.timeInMillis, event.period)
                }

            if(calendarStart.timeInMillis>=calendarNow.timeInMillis){
                calendarAlarmTime.timeInMillis = calendarStart.timeInMillis
                setAlarm(context, tittle, calendarAlarmTime.timeInMillis, event.id, event.type)
            } else {
                val razn = calendarFinish.timeInMillis - calendarNow.timeInMillis
                if (razn > 0) {
                    if (TimeUnit.MILLISECONDS.toMinutes(razn) > periodSetting) {
                        calendarAlarmTime.add(Calendar.MINUTE, periodSetting)
                    } else {
                        calendarAlarmTime.timeInMillis = calendarFinish.timeInMillis
                    }
                    //calendarAlarmTime.timeInMillis=DateUtils.getMilsWithAdding(calendarAlarmTime.timeInMillis, beforeId)
                    setAlarm(context, tittle, calendarAlarmTime.timeInMillis, event.id, event.type)
                } else {
                    if (event.period > 0) {
                        calendarAlarmTime.timeInMillis =
                            DateUtils.getMilsWithAddingPeriod(
                                calendarStart.timeInMillis,
                                event.period
                            )
                        calendarAlarmTime.timeInMillis = DateUtils.getMilsWithAdding(
                            calendarAlarmTime.timeInMillis,
                            event.notifyBefore
                        )
                        setAlarm(
                            context,
                            tittle,
                            calendarAlarmTime.timeInMillis,
                            event.id,
                            event.type
                        )
                    }
                }
            }
        }

        private fun alarmRepeatHoliday(context: Context, tittle:String, event: Holiday, isRepeating:Boolean){
            setAlarm(
                context,
                tittle,
                DateUtils.getAlarmDateTimeForHoliday(
                    event,
                    SharedPreferencesUtils.getNotifyHolidayTime(context),
                    isRepeating),
                event.id,
                event.type)
        }

        fun updateHolidayAlarm(context: Context, activeAlarmHoliday:List<Holiday>, listHolidayByType:List<Holiday>, time:Long){
            activeAlarmHoliday.forEach {
                cancelAlarm(
                    context,
                    it.name ?: context.resources.getString(R.string.tv_event_name_null),
                    it.id,
                    it.type
                )
            }
            listHolidayByType.forEach { holiday ->
                holiday.isAlarm = true
                setAlarm(
                    context,
                    holiday!!.name
                        ?: context.resources.getString(R.string.tv_event_name_null),
                    DateUtils.getAlarmDateTimeForHoliday(holiday, time, false),
                    holiday!!.id,
                    holiday!!.type
                )
            }
        }
    }
}