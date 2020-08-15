package com.vnprk.holidays.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.vnprk.holidays.App
import com.vnprk.holidays.MainActivity
import com.vnprk.holidays.R
import com.vnprk.holidays.Repository
import com.vnprk.holidays.Room.LocalDb
import javax.inject.Inject


class AlarmService : IntentService("AlarmService") {

    var media_song: MediaPlayer? = null
    var startId = 0
    var isRunning = false
    @Inject
    lateinit var localDb : LocalDb
    override fun onCreate() {
        super.onCreate()
        App.instance.appComponent.inject(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onHandleIntent(intent: Intent?) {
        var title = ""
        val timeRepeatingSetting = 5 //min
        intent?.let {intent ->
            val idEvent = intent.extras.getInt(AlarmBroadcastReceiver.Companion.ID_EVENT,0)
            val event = localDb.getPrivateById(idEvent)
            event?.let{
                if(event.isActive) {
                    AlarmUtils.setAlarmRepeat(
                        this,
                        event.name?:this.resources.getString(R.string.tv_event_name_null),
                        event.startDateTime,
                        event.finishDateTime,
                        event.period,
                        timeRepeatingSetting,
                        event.notifyBefore,
                        event.id
                    )
                    title = intent.getStringExtra(AlarmBroadcastReceiver.Companion.TITLE)
                    sendNotification(title, "тест")
                }
            }
        }
    }

   /* @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        var startId = startId
        Log.i("LocalService", "Received start id $startId: $intent")

        // fetch the extra string from the alarm on/alarm off values
        val state = intent.extras.getString("extra")
        // fetch the whale choice integer values
        val whale_sound_choice = intent.extras.getInt("whale_choice")
        Log.e("Ringtone extra is ", state)
        Log.e("Whale choice is ", whale_sound_choice.toString())

        // put the notification here, test it out

        // notification
        // set up the notification service
        val notify_manager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        // set up an intent that goes to the Main Activity
        val intent_main_activity =
            Intent(this.getApplicationContext(), MainActivity::class.java)
        // set up a pending intent
        val pending_intent_main_activity = PendingIntent.getActivity(
            this, 0,
            intent_main_activity, 0
        )

        // make the notification parameters
        val notification_popup: Notification = Builder(this)
            .setContentTitle("An alarm is going off!")
            .setContentText("Click me!")
            .setSmallIcon(R.drawable.ic_action_call)
            .setContentIntent(pending_intent_main_activity)
            .setAutoCancel(true)
            .build()
        assert(state != null)
        when (state) {
            "alarm on" -> startId = 1
            "alarm off" -> {
                startId = 0
                Log.e("Start ID is ", state)
            }
            else -> startId = 0
        }
        // if else statements

        // if there is no music playing, and the user pressed "alarm on"
        // music should start playing
        if (!isRunning && startId == 1) {
            Log.e("there is no music, ", "and you want start")
            isRunning = true
            this.startId = 0

            // set up the start command for the notification
            notify_manager!!.notify(0, notification_popup)
            // play the whale sound depending on the passed whale choice id
            if (whale_sound_choice == 0) {
                // play a randomly picked audio file
                val minimum_number = 1
                val maximum_number = 13
                val random_number = Random()
                val whale_number: Int = random_number.nextInt(maximum_number + minimum_number)
                Log.e("random number is ", whale_number.toString())
                if (whale_number == 1) {
                    media_song = MediaPlayer.create(this, R.raw.humpback_bubblenet_and_vocals)
                    media_song!!.start()
                } else if (whale_number == 2) {
                    // create an instance of the media player
                    media_song = MediaPlayer.create(this, R.raw.humpback_contact_call_moo)
                    // start the ringtone
                    media_song!!.start()
                } else if (whale_number == 3) {
                    media_song = MediaPlayer.create(this, R.raw.humpback_contact_call_whup)
                    media_song!!.start()
                } else if (whale_number == 4) {
                    media_song = MediaPlayer.create(this, R.raw.humpback_feeding_call)
                    media_song!!.start()
                } else if (whale_number == 5) {
                    media_song = MediaPlayer.create(this, R.raw.humpback_flipper_splash)
                    media_song!!.start()
                } else if (whale_number == 6) {
                    media_song =
                        MediaPlayer.create(this, R.raw.humpback_tail_slaps_with_propeller_whine)
                    media_song!!.start()
                } else if (whale_number == 7) {
                    media_song = MediaPlayer.create(this, R.raw.humpback_whale_song)
                    media_song!!.start()
                } else if (whale_number == 8) {
                    media_song = MediaPlayer.create(
                        this,
                        R.raw.humpback_whale_song_with_outboard_engine_noise
                    )
                    media_song!!.start()
                } else if (whale_number == 9) {
                    media_song = MediaPlayer.create(this, R.raw.humpback_wheeze_blows)
                    media_song!!.start()
                } else if (whale_number == 10) {
                    media_song = MediaPlayer.create(this, R.raw.killerwhale_echolocation_clicks)
                    media_song!!.start()
                } else if (whale_number == 11) {
                    media_song = MediaPlayer.create(this, R.raw.killerwhale_offshore)
                    media_song!!.start()
                } else if (whale_number == 12) {
                    media_song = MediaPlayer.create(this, R.raw.killerwhale_resident)
                    media_song!!.start()
                } else if (whale_number == 13) {
                    media_song = MediaPlayer.create(this, R.raw.killerwhale_transient)
                    media_song!!.start()
                } else {
                    media_song = MediaPlayer.create(this, R.raw.killerwhale_resident)
                    media_song!!.start()
                }
            } else if (whale_sound_choice == 1) {
                // create an instance of the media player
                media_song = MediaPlayer.create(this, R.raw.humpback_bubblenet_and_vocals)
                // start the ringtone
                media_song!!.start()
            } else if (whale_sound_choice == 2) {
                // create an instance of the media player
                media_song = MediaPlayer.create(this, R.raw.humpback_contact_call_moo)
                // start the ringtone
                media_song!!.start()
            } else if (whale_sound_choice == 3) {
                media_song = MediaPlayer.create(this, R.raw.humpback_contact_call_whup)
                media_song!!.start()
            } else if (whale_sound_choice == 4) {
                media_song = MediaPlayer.create(this, R.raw.humpback_feeding_call)
                media_song!!.start()
            } else if (whale_sound_choice == 5) {
                media_song = MediaPlayer.create(this, R.raw.humpback_flipper_splash)
                media_song!!.start()
            } else if (whale_sound_choice == 6) {
                media_song =
                    MediaPlayer.create(this, R.raw.humpback_tail_slaps_with_propeller_whine)
                media_song!!.start()
            } else if (whale_sound_choice == 7) {
                media_song = MediaPlayer.create(this, R.raw.humpback_whale_song)
                media_song!!.start()
            } else if (whale_sound_choice == 8) {
                media_song =
                    MediaPlayer.create(this, R.raw.humpback_whale_song_with_outboard_engine_noise)
                media_song!!.start()
            } else if (whale_sound_choice == 9) {
                media_song = MediaPlayer.create(this, R.raw.humpback_wheeze_blows)
                media_song!!.start()
            } else if (whale_sound_choice == 10) {
                media_song = MediaPlayer.create(this, R.raw.killerwhale_echolocation_clicks)
                media_song!!.start()
            } else if (whale_sound_choice == 11) {
                media_song = MediaPlayer.create(this, R.raw.killerwhale_offshore)
                media_song!!.start()
            } else if (whale_sound_choice == 12) {
                media_song = MediaPlayer.create(this, R.raw.killerwhale_resident)
                media_song!!.start()
            } else if (whale_sound_choice == 13) {
                media_song = MediaPlayer.create(this, R.raw.killerwhale_transient)
                media_song!!.start()
            } else {
                media_song = MediaPlayer.create(this, R.raw.killerwhale_resident)
                media_song!!.start()
            }
        } else if (isRunning && startId == 0) {
            Log.e("there is music, ", "and you want end")
            // stop the ringtone
            media_song!!.stop()
            media_song!!.reset()
            isRunning = false
            this.startId = 0
        } else if (!isRunning && startId == 0) {
            Log.e("there is no music, ", "and you want end")
            isRunning = false
            this.startId = 0
        } else if (isRunning && startId == 1) {
            Log.e("there is music, ", "and you want start")
            isRunning = true
            this.startId = 1
        } else {
            Log.e("else ", "somehow you reached this")
        }
        return START_NOT_STICKY
    }*/

    private fun sendNotification(messageTittle: String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
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
                    R.drawable.test_gradient
                )
            )
            .setContentTitle(/*getString(R.string.fcm_notify_tittle)*/messageTittle)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setContentIntent(pendingIntent)

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
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun getNotificationIcon(): Int {
        val useWhiteIcon =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        return if (useWhiteIcon) R.drawable.ic_launcher_background else R.drawable.ic_launcher_foreground
    }

    override fun onDestroy() {
        // Tell the user we stopped.
        Log.e("on Destroy called", "ta da")
        super.onDestroy()
        isRunning = false
    }
}