package com.vnprk.holidays.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.vnprk.holidays.R
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmUtils {
    companion object {
        fun setAlarm(context: Context, tittle:String, dateTimeMils: Long, eventId: Int) {
            val alarmManager: AlarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val myIntent = Intent(context, AlarmBroadcastReceiver::class.java)
            myIntent.putExtra(AlarmBroadcastReceiver.Companion.ID_EVENT, eventId)
            myIntent.putExtra(AlarmBroadcastReceiver.Companion.TITLE, tittle)
            val pendingIntent =
                PendingIntent.getBroadcast(context, eventId, myIntent, 0)
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

        fun setAlarmRepeat(
            context: Context,
            tittle:String,
            startDateTimeMils: Long,
            finishDateTimeMils: Long,
            period:Int,
            periodSetting:Int,
            beforeId:Int,
            eventId: Int){
            val calendarAlarmTime = Calendar.getInstance()
            val calendarStart = Calendar.getInstance()
            calendarStart.timeInMillis=startDateTimeMils
            val calendarFinish = Calendar.getInstance()
            calendarFinish.timeInMillis=finishDateTimeMils
            val calendarNow = Calendar.getInstance()
            calendarStart.set(Calendar.YEAR, calendarNow.get(Calendar.YEAR))
            calendarStart.set(Calendar.MONTH, calendarNow.get(Calendar.MONTH))
            calendarStart.set(Calendar.DAY_OF_MONTH, calendarNow.get(Calendar.DAY_OF_MONTH))
            if(finishDateTimeMils < calendarStart.timeInMillis && period>0)
                while(calendarFinish.timeInMillis < calendarStart.timeInMillis){
                    calendarFinish.timeInMillis=DateUtils.getMilsWithAddingPeriod(calendarFinish.timeInMillis, period)
                }

            val razn = calendarFinish.timeInMillis - calendarNow.timeInMillis
            if(razn>0) {
                if (TimeUnit.MILLISECONDS.toMinutes(razn) > periodSetting) {
                    calendarAlarmTime.add(Calendar.MINUTE, periodSetting)
                } else {
                    calendarAlarmTime.timeInMillis = calendarFinish.timeInMillis
                }
                //calendarAlarmTime.timeInMillis=DateUtils.getMilsWithAdding(calendarAlarmTime.timeInMillis, beforeId)
                setAlarm(context, tittle, calendarAlarmTime.timeInMillis, eventId)
            }
            else{
                if(period>0){
                    calendarAlarmTime.timeInMillis=DateUtils.getMilsWithAddingPeriod(calendarStart.timeInMillis, period)
                    calendarAlarmTime.timeInMillis=DateUtils.getMilsWithAdding(calendarAlarmTime.timeInMillis, beforeId)
                    setAlarm(context, tittle, calendarAlarmTime.timeInMillis, eventId)
                }
            }

        }
    }
}