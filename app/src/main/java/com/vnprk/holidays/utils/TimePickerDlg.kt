package com.vnprk.holidays.utils

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerDlg: DialogFragment(), TimePickerDialog.OnTimeSetListener
{
    private var viewCode =0
    lateinit var calendar: Calendar
    companion object{
        val KEY_TIME="key_time"
        val KEY_VIEW="key_view_code"
        fun newInstance(dateInMils:Long, viewCode:Int):TimePickerDlg{
            val args = Bundle()
            args.putLong(KEY_TIME, dateInMils)
            args.putInt(KEY_VIEW, viewCode)
            val fragment = TimePickerDlg()
            fragment.arguments=args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dateInMils = arguments?.getLong(KEY_TIME,0)
        viewCode = arguments?.getInt(KEY_VIEW, 0)!!
        calendar = Calendar.getInstance()
        calendar.timeInMillis = dateInMils!!
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return TimePickerDialog(activity,this, hourOfDay, minute, true)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        //val calendar= Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        this.mListener!!.onTimeComplete(calendar, viewCode)
    }

    fun setListener(listener:OnTimeCompleteListener){
        mListener=listener
    }

    interface OnTimeCompleteListener {
        fun onTimeComplete( time:Calendar, viewCode:Int)
    }

    private var mListener: OnTimeCompleteListener? = null

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}