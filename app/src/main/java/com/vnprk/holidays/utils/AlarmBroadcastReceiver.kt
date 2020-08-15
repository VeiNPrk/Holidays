package com.vnprk.holidays.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast


class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        startAlarmService(context, intent)
        /*if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            val toastText = String.format("Alarm Reboot")
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
            startRescheduleAlarmsService(context)
        } else {
            val toastText = String.format("Alarm Received")
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
            if (!intent.getBooleanExtra(AlarmBroadcastReceiver.Companion.RECURRING, false)) {
                startAlarmService(context, intent)
            }
            /*run {
                if (alarmIsToday(intent)) {
                    startAlarmService(context, intent)
                }
            }*/
        }*/
    }

    /*private fun alarmIsToday(intent: Intent): Boolean {
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(System.currentTimeMillis())
        val today: Int = calendar.get(Calendar.DAY_OF_WEEK)
        when (today) {
            Calendar.MONDAY -> {
                return if (intent.getBooleanExtra(
                        AlarmBroadcastReceiver.Companion.MONDAY,
                        false
                    )
                ) true else false
            }
            Calendar.TUESDAY -> {
                return if (intent.getBooleanExtra(
                        AlarmBroadcastReceiver.Companion.TUESDAY,
                        false
                    )
                ) true else false
            }
            Calendar.WEDNESDAY -> {
                return if (intent.getBooleanExtra(
                        AlarmBroadcastReceiver.Companion.WEDNESDAY,
                        false
                    )
                ) true else false
            }
            Calendar.THURSDAY -> {
                return if (intent.getBooleanExtra(
                        AlarmBroadcastReceiver.Companion.THURSDAY,
                        false
                    )
                ) true else false
            }
            Calendar.FRIDAY -> {
                return if (intent.getBooleanExtra(
                        AlarmBroadcastReceiver.Companion.FRIDAY,
                        false
                    )
                ) true else false
            }
            Calendar.SATURDAY -> {
                return if (intent.getBooleanExtra(
                        AlarmBroadcastReceiver.Companion.SATURDAY,
                        false
                    )
                ) true else false
            }
            Calendar.SUNDAY -> {
                return if (intent.getBooleanExtra(
                        AlarmBroadcastReceiver.Companion.SUNDAY,
                        false
                    )
                ) true else false
            }
        }
        return false
    }*/

    private fun startAlarmService(context: Context, intent: Intent) {
        val intentService = Intent(context, AlarmService::class.java)
        intentService.putExtra(TITLE, intent.getStringExtra(TITLE))
        intentService.putExtra(ID_EVENT, intent.getIntExtra(ID_EVENT, 0))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }

    /*private fun startRescheduleAlarmsService(context: Context) {
        val intentService = Intent(context, RescheduleAlarmsService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }*/

    companion object {
        const val MONDAY = "MONDAY"
        const val TUESDAY = "TUESDAY"
        const val WEDNESDAY = "WEDNESDAY"
        const val THURSDAY = "THURSDAY"
        const val FRIDAY = "FRIDAY"
        const val SATURDAY = "SATURDAY"
        const val SUNDAY = "SUNDAY"
        const val RECURRING = "RECURRING"
        const val TITLE = "TITLE"
        const val ID_EVENT = "ID_EVENT"
    }
}