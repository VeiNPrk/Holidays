package com.vnprk.holidays.views

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.vnprk.holidays.BR
import com.vnprk.holidays.R
import com.vnprk.holidays.databinding.FragmentEditPrivateBinding
import com.vnprk.holidays.models.Status
import com.vnprk.holidays.utils.DatePickerDlg
import com.vnprk.holidays.utils.NotifysDlg
import com.vnprk.holidays.utils.PeriodsDlg
import com.vnprk.holidays.utils.TimePickerDlg
import com.vnprk.holidays.viewmodels.PrivateEventViewModel
import java.util.*


class PrivateEventEditFragment : Fragment(), DatePickerDlg.OnDateCompleteListener, TimePickerDlg.OnTimeCompleteListener,
PeriodsDlg.OnPeriodListener, NotifysDlg.OnNotifyListener{

    var idEvent = 0
    lateinit var binding: FragmentEditPrivateBinding
    private val viewModel: PrivateEventViewModel by viewModels()

    /*companion object {
        const val KEY_ID_EVENT = "id_event_key"
        fun newInstance(idHoliday:Int): PrivateEventEditFragment {
            val myFragment = PrivateEventEditFragment()
            val args = Bundle()
            args.putInt(KEY_ID_EVENT, idHoliday)
            myFragment.arguments = args
            return myFragment
        }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        idEvent = arguments?.getInt("key_id_event", 0)!!
        if(idEvent==0)
            idEvent = arguments?.getInt("idEvent", 0)!!
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_private, container, false)
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
        binding.tvStartDateEvent.setOnClickListener {
            showDatePickerDialog(viewModel.privateEvent!!.startDateTime, viewModel.VIEW_CODE_START_DATE)
        }
        binding.tvStartTimeEvent.setOnClickListener {
            showTimePickerDialog(viewModel.privateEvent!!.startDateTime, viewModel.VIEW_CODE_START_TIME)
        }
        binding.tvFinishDateEvent.setOnClickListener {
            showDatePickerDialog(viewModel.privateEvent!!.finishDateTime, viewModel.VIEW_CODE_FINISH_DATE)
        }
        binding.tvFinishTimeEvent.setOnClickListener {
            showTimePickerDialog(viewModel.privateEvent!!.finishDateTime, viewModel.VIEW_CODE_FINISH_TIME)
        }
        binding.layoutPeriod.setOnClickListener {
            showPeriodsDialog()
        }
        binding.layoutNotifyBefore.setOnClickListener {
            showNotifysDialog()
        }
        binding.layoutColor.setOnClickListener {
            showColorDialog()
        }
        binding.btnSaveEvent.setOnClickListener{
            closeKeyBoard()
            viewModel.saveEvent()
        }
        return binding.root
    }

    private fun showColorDialog() {
        viewModel.getColorDialog(activity as Activity).show()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_menu_date).isVisible=false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getPrivateEvent(idEvent).observe(viewLifecycleOwner, Observer { holiday ->
            holiday?.let {
                binding.setVariable(BR.event, it)
                binding.executePendingBindings()
            }
        })
        viewModel.loadStateLiveData.observe(viewLifecycleOwner, Observer { state->
            state.let {
                when (state.status) {
                    Status.RUNNING -> {
                    }
                    Status.SUCCESS -> {
                        if(state.msg==viewModel.ACTION_SAVE_COMPLETE)
                        {
                            viewModel.setAlarm(context)
                            val navController =
                                activity?.let { it1 -> Navigation.findNavController(it1, R.id.nav_host_fragment) }
                            var action = PrivateEventEditFragmentDirections.actionPrivateEventEditFragmentToNavCelebrationPrivate()
                            navController?.popBackStack()
                            //navController?.navigate(action)
                        }
                        else
                            showMessage(state.msg)
                    }
                    Status.FAILED -> {
                        showMessage(state.msg)
                        //swipeLayout.isRefreshing=false
                        //pbSearch.visibility=View.GONE
                    }
                }
            }
        })
    }

    private fun closeKeyBoard() {
        val view = activity?.currentFocus
        if (view != null) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showPeriodsDialog(){
        viewModel.getPeriodDialog(this).show(parentFragmentManager, "periodsDlg")
    }

    override fun onPeriodClick(index: Int) {
        viewModel.setPeriod(index)
    }

    private fun showNotifysDialog(){
        viewModel.getNotifyDialog(this).show(parentFragmentManager, "notifysDlg")
    }

    override fun onNotifyClick(index: Int) {
        viewModel.setNotify(index)
    }

    private fun showDatePickerDialog(date: Long, viewCode:Int) {
        viewModel.getDateDialog(this, date, viewCode).show(parentFragmentManager, "datePicker")
    }

    private fun showTimePickerDialog(date: Long, viewCode:Int) {
        viewModel.getTimeDialog(this, date, viewCode).show(parentFragmentManager, "timePicker")
    }

    override fun onDateComplete(time: Calendar, viewCode:Int) {
        viewModel.setDateEvent(time, viewCode)
    }

    override fun onTimeComplete(time: Calendar, viewCode: Int) {
        viewModel.setTimeEvent(time, viewCode)
    }

    private fun showMessage(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
    }


}