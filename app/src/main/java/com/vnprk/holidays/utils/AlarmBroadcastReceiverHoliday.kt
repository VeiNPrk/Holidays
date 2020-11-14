package com.vnprk.holidays.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast


class AlarmBroadcastReceiverHoliday : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        startAlarmService(context, intent)
    }

    private fun startAlarmService(context: Context, intent: Intent) {
        val intentService = Intent(context, AlarmService::class.java)
        intentService.putExtra(AlarmUtils.TITLE, intent.getStringExtra(AlarmUtils.TITLE))
        intentService.putExtra(AlarmUtils.ID_EVENT, intent.getIntExtra(AlarmUtils.ID_EVENT, 0))
        intentService.putExtra(AlarmUtils.TYPE_EVENT, intent.getIntExtra(AlarmUtils.TYPE_EVENT, 0))
        intentService.putExtra(AlarmUtils.KEY_ACTION_NOTIFY, intent.getStringExtra(AlarmUtils.KEY_ACTION_NOTIFY))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }
}