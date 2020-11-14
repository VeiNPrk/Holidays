package com.vnprk.holidays.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast


class DeviceRebootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        startRebootService(context, intent)
    }

    private fun startRebootService(context: Context, intent: Intent) {
        val intentService = Intent(context, AlarmRebootService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }
}