package com.vnprk.holidays.utils

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.view.get
import androidx.preference.PreferenceDialogFragmentCompat
import java.text.SimpleDateFormat
import java.util.*

class TimePreferenceDialogFragment : PreferenceDialogFragmentCompat() {
    private var mHour = 0;
    private var mMinute = 0;
    private var mTimePicker: TimePicker? = null
    private lateinit var calendar:Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var timeValueMils = timePreference.getTime()
        calendar = Calendar.getInstance()
        if (timeValueMils == null || timeValueMils==0L) {
            timeValueMils = calendar.timeInMillis/*"${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"*/
        } else calendar.timeInMillis=timeValueMils
        mHour = calendar.get(Calendar.HOUR_OF_DAY)//getHour(timeValue!!)
        mMinute = calendar.get(Calendar.MINUTE)//getMinute(timeValue!!)
    }

    override fun onCreateDialogView(context: Context?): View {
        mTimePicker = TimePicker(getContext())
        mTimePicker?.setIs24HourView(true)
        // Show spinner dialog for old APIs.
        //mTimePicker!!.calendarViewShown = false
        return mTimePicker as TimePicker
    }

    override fun onBindDialogView(view: View?) {
        super.onBindDialogView(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker!!.hour=calendar.get(Calendar.HOUR_OF_DAY)//mHour
            mTimePicker!!.minute=calendar.get(Calendar.MINUTE)//mMinute
        }else{
            mTimePicker!!.currentHour=calendar.get(Calendar.HOUR_OF_DAY)//mHour
            mTimePicker!!.currentMinute=calendar.get(Calendar.MINUTE)//mMinute
        }
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            mHour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mTimePicker!!.hour
            } else{
                mTimePicker!!.currentHour
            }
            calendar.set(Calendar.HOUR_OF_DAY, mHour)
            mMinute = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mTimePicker!!.minute
            } else{
                mTimePicker!!.currentMinute
            }
            calendar.set(Calendar.MINUTE, mMinute)
            //val dateVal = "$mHour:$mMinute"
            val dateVal = calendar.timeInMillis
            val preference = timePreference
            if (preference.callChangeListener(dateVal)) {
                preference.setTime(dateVal)
            }
        }
    }
/*
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        //val calendar= Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        this.mListener!!.onTimeComplete(calendar, viewCode)
    }*/

    private val timePreference: TimePreference
        private get() = preference as TimePreference
/*
    private fun getHour(dateString: String): Int {
        val datePieces = dateString.split(":".toRegex()).toTypedArray()
        return datePieces[0].toInt()
    }

    private fun getMinute(dateString: String): Int {
        val datePieces = dateString.split(":".toRegex()).toTypedArray()
        return datePieces[1].toInt()
    }*/

    companion object {
        fun newInstance(key: String?): TimePreferenceDialogFragment {
            val fragment = TimePreferenceDialogFragment()
            val b = Bundle(1)
            b.putString(ARG_KEY, key)
            fragment.arguments = b
            return fragment
        }
    }
}