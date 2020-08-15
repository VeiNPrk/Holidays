package com.vnprk.holidays.utils

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.vnprk.holidays.models.Event
import java.util.*

class PeriodsDlg: DialogFragment()
{
    lateinit var calendar :Calendar
    companion object{
        val KEY_PERIOD="key_period"
        fun newInstance(period:Int):PeriodsDlg{
            val args = Bundle()
            args.putInt(KEY_PERIOD, period)
            val fragment = PeriodsDlg()
            fragment.arguments=args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder=AlertDialog.Builder(requireContext())
        val period = arguments?.getInt(KEY_PERIOD, 0)!!
        builder//.setTitle("Выберите период")
            .setSingleChoiceItems(Event.periods.values.toTypedArray(),
                period,
                DialogInterface.OnClickListener { dialog, which ->
                    mListener?.onPeriodClick(which)
                    dismiss()
                    // The 'which' argument contains the index position
                    // of the selected item
                })
        return builder.create()
    }

    fun setListener(listener:OnPeriodListener){
        mListener=listener
    }

    interface OnPeriodListener {
        fun onPeriodClick(index:Int)
    }

    private var mListener: OnPeriodListener? = null

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}