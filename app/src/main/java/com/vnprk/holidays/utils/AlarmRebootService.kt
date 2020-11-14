package com.vnprk.holidays.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.vnprk.holidays.App
import com.vnprk.holidays.MainActivity
import com.vnprk.holidays.R
import com.vnprk.holidays.Room.LocalDb
import com.vnprk.holidays.models.Event
import javax.inject.Inject


class AlarmRebootService : IntentService("AlarmService") {
    @Inject
    lateinit var localDb : LocalDb
    override fun onCreate() {
        super.onCreate()
        App.instance.appComponent.inject(this)
        if (Build.VERSION.SDK_INT >= 26) {
            val CHANNEL_ID = "my_channel_01"
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel
            )
            val notification =
                NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build()
            startForeground(1, notification)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onHandleIntent(intent: Intent?) {
        val timeRepeatingSetting = SharedPreferencesUtils.getInnerRepeats(this)
        val activePrivateEvents = localDb.getPrivateActiveAlarm()
        val activeHolidays = localDb.getHolidayActiveAlarm()
        activePrivateEvents.forEach {
            AlarmUtils.setAlarmRepeat(this,true,
                it.name?:this.resources.getString(R.string.tv_event_name_null),
                it,
                timeRepeatingSetting
            )
        }
        activeHolidays.forEach {
            AlarmUtils.setAlarmRepeat(this,false,
                it.name?:this.resources.getString(R.string.tv_event_name_null),
                it,
                timeRepeatingSetting,
                false
            )
        }
        Log.d("AlarmRebootService", "Service finished")
    }
    override fun onDestroy() {
        Log.d("AlarmRebootService", "on Destroy called")
        super.onDestroy()
    }
}