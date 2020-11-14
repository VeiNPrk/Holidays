package com.vnprk.holidays.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
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


class AlarmService : IntentService("AlarmService") {
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
        intent?.let { intent ->
            val idEvent = intent.getIntExtra(AlarmUtils.ID_EVENT, 0)
            val title = intent.getStringExtra(AlarmUtils.TITLE)
            val type = intent.getIntExtra(AlarmUtils.TYPE_EVENT, 0)
            var notificationMessage: String = ""
            if (type == Event.PRIVATE_TYPE) {
                if (intent.getStringExtra(AlarmUtils.KEY_ACTION_NOTIFY) == AlarmUtils.ACTION_NOTIFY_CANCEL) {
                    localDb.disactiveEventById(idEvent)
                    AlarmUtils.cancelAlarm(this, title, idEvent, type)
                    (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(0)
                } else {
                    val event = localDb.getPrivateById(idEvent)
                    notificationMessage = "${event.getStrStartTime()} - ${event.getStrFinishTime()}"
                    if (event.isActive) {
                        AlarmUtils.setAlarmRepeat(
                            this,
                            true,
                            title,
                            event,
                            timeRepeatingSetting
                        )
                    }
                    sendNotification(title, notificationMessage, idEvent, type)
                }
            } else {
                val holiday = localDb.getHolidayId(idEvent)
                notificationMessage = DateUtils.getStrNameDate(this, DateUtils.getNowDateTimeMills(), true)
                if (holiday.isAlarm) {
                    AlarmUtils.setAlarmRepeat(
                        this,
                        false,
                        title,
                        holiday,
                        timeRepeatingSetting
                    )
                }
                sendNotification(notificationMessage, title, idEvent, type)
            }
        }
        stopSelf()
    }

    private fun sendNotification(messageTittle: String, messageBody: String, idEvent:Int, typeEvent:Int) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(AlarmUtils.FROM_NOTIF, true)
        intent.putExtra(AlarmUtils.ID_EVENT, idEvent)
        intent.putExtra(AlarmUtils.TYPE_EVENT, typeEvent)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(getNotificationIcon())
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_launcher_round
                )
            )
            .setContentTitle(/*getString(R.string.fcm_notify_tittle)*/messageTittle)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setContentIntent(pendingIntent)
        if(typeEvent == Event.PRIVATE_TYPE) {
            val actionIntent = Intent(this, AlarmBroadcastReceiverPrivate::class.java)
            actionIntent.putExtra(AlarmUtils.ID_EVENT, idEvent)
            actionIntent.putExtra(AlarmUtils.TYPE_EVENT, typeEvent)
            actionIntent.putExtra(AlarmUtils.TITLE, messageTittle)
            actionIntent.putExtra(AlarmUtils.KEY_ACTION_NOTIFY, AlarmUtils.ACTION_NOTIFY_CANCEL)
            val actionPendingIntent =
                PendingIntent.getBroadcast(this, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            notificationBuilder.addAction(
                R.drawable.button, getString(R.string.notification_action_btn_cancel),
                actionPendingIntent
            )
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun getNotificationIcon(): Int {
        val useWhiteIcon =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        return R.drawable.ic_baseline_event_24/*if (useWhiteIcon) R.drawable.ic_launcher_background else R.drawable.ic_launcher_foreground*/
    }

    override fun onDestroy() {
        Log.e("AlarmService", "on Destroy called")
        super.onDestroy()
    }
}