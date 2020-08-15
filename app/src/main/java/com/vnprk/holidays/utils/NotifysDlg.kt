package com.vnprk.holidays.utils

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.vnprk.holidays.models.Event
import java.util.*

class NotifysDlg: DialogFragment()
{
    lateinit var calendar :Calendar
    companion object{
        val KEY_NOTIFY="key_notify"
        fun newInstance(period:Int):NotifysDlg{
            val args = Bundle()
            args.putInt(KEY_NOTIFY, period)
            val fragment = NotifysDlg()
            fragment.arguments=args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder=AlertDialog.Builder(requireContext())
        val period = arguments?.getInt(KEY_NOTIFY, 0)!!
        builder//.setTitle("Выберите период")
            .setSingleChoiceItems(Event.notifys.values.toTypedArray(),
                period,
                DialogInterface.OnClickListener { dialog, which ->
                    mListener?.onNotifyClick(which)
                    dismiss()
                    // The 'which' argument contains the index position
                    // of the selected item
                })
        return builder.create()
    }

    fun setListener(listener:OnNotifyListener){
        mListener=listener
    }

    interface OnNotifyListener {
        fun onNotifyClick(index:Int)
    }

    private var mListener: OnNotifyListener? = null

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}