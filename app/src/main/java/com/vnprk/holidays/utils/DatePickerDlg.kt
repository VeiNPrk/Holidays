package com.vnprk.holidays.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerDlg: DialogFragment(), DatePickerDialog.OnDateSetListener
{
    private var viewCode = 0
    lateinit var calendar :Calendar
    companion object{
        val KEY_DATE="key_date"
        val KEY_VIEW="key_view_code"
        fun newInstance(dateInMils:Long, viewCode:Int):DatePickerDlg{
            val args = Bundle()
            args.putLong(KEY_DATE, dateInMils)
            args.putInt(KEY_VIEW, viewCode)
            val fragment = DatePickerDlg()
            fragment.arguments=args
            return fragment
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val dateInMils = arguments?.getLong(KEY_DATE, 0)
        viewCode = arguments?.getInt(KEY_VIEW, 0)!!
        calendar = Calendar.getInstance()
        calendar.timeInMillis=dateInMils!!
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(activity,this,year,month,day)
    }

    override  fun onDateSet(view: DatePicker, year:Int, month:Int, day:Int)
    {
        //val calendar= Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        //onDateComplete()
        this.mListener!!.onDateComplete(calendar, viewCode);
    }

    fun setListener(listener:OnDateCompleteListener){
        mListener=listener
    }

    interface OnDateCompleteListener {
        fun onDateComplete( time:Calendar, viewCode:Int)
    }

    private var mListener: OnDateCompleteListener? = null

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}