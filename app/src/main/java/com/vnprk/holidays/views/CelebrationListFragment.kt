package com.vnprk.holidays.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vnprk.holidays.R
import com.vnprk.holidays.models.Status
import com.vnprk.holidays.utils.DatePickerDlg
import com.vnprk.holidays.utils.DateUtils
import com.vnprk.holidays.utils.EventsRecyclerAdapter
import com.vnprk.holidays.utils.NetworkUtils
import com.vnprk.holidays.viewmodels.CelebrationListViewModel
import kotlinx.android.synthetic.main.fragment_private_list.view.*
import java.util.*

class CelebrationListFragment : Fragment(), EventsRecyclerAdapter.EventDetailClickListener {

    private val viewModel: CelebrationListViewModel by activityViewModels()
    private lateinit var mAdapter: EventsRecyclerAdapter
    private lateinit var rv: RecyclerView
    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var tvMessage: TextView
    private var isSwipe = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.title="12345"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_celebration_list, container, false)
        tvMessage = root.tv_msg
        var type: Int = -1
        if (arguments != null) {
            type = arguments?.getInt("typeCelebrate", -1)!!
        }

        rv = root.findViewById(R.id.rv_holiday_events)
        swipeLayout = root.findViewById(R.id.swipe_refresh_layout)
        swipeLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        swipeLayout.setOnRefreshListener {
            Log.i("ControlList", "onRefresh called from SwipeRefreshLayout")
            isSwipe = true
            tvMessage.visibility=View.GONE
            viewModel.onRefreshLayout()
        }
        setRecyclerView()
        if(!viewModel.getLiveDataNowDate().hasObservers())
            viewModel.getLiveDataNowDate().observe(viewLifecycleOwner, Observer { nowDate ->
                //activity?.title=DateUtils.getStrNameDate(requireContext(), nowDate)
            })
        //viewModel.setDateEvent(requireContext(), Calendar.getInstance())
        NetworkUtils.networkState.observe(viewLifecycleOwner, Observer { nState ->
            NetworkUtils.networkState.let {
                when (nState.status) {
                    Status.RUNNING -> {
                        Log.d("coroutine", "Status.RUNNING")
                        swipeLayout.isRefreshing=true
                    }
                    Status.SUCCESS -> {
                        Log.d("coroutine", "Status.SUCCESS")
                        swipeLayout.isRefreshing=false
                    }
                    Status.FAILED -> {
                        Log.d("coroutine", "Status.FAILED")
                        swipeLayout.isRefreshing=false
                    }
                }
            }
        })
        viewModel.getEventByType(type).observe(viewLifecycleOwner, Observer {
            swipeLayout.isRefreshing=false
            mAdapter.setDetailsData(it)
            if(it.isEmpty())
            {
                tvMessage.text=getString(R.string.no_have_celebrate)
                tvMessage.visibility=View.VISIBLE
            }
            else tvMessage.visibility=View.GONE
        })
        return root
    }

    fun setRecyclerView() {
        val mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mAdapter =
            context?.let { EventsRecyclerAdapter(requireContext(),this, null) }!!
        rv.setHasFixedSize(true)
        rv.layoutManager = mLayoutManager
        rv.adapter = mAdapter
        rv.itemAnimator = DefaultItemAnimator()
    }

    /*override fun onOptionsItemSelected(item: MenuItem)=
    when (item.itemId) {
        R.id.action_settings -> {
            // User chose the "Settings" item, show the app settings UI...
            true
        }

        R.id.action_date -> {
            showDatePickerDialog(0)
            // User chose the "Favorite" action, mark the current item
            // as a favorite...
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }*/

    /*private fun showDatePickerDialog(/*date: Long, */viewCode:Int) {
        viewModel.getDateDialog(this).show(parentFragmentManager, "datePicker")
    }*/

    override fun onDetailClick(idEvent: Int, type:Int) {
        val bottomDialogFragment =
            CelebrationViewFragment.newInstance(idEvent)
        bottomDialogFragment
        bottomDialogFragment.show(
            parentFragmentManager,
            "ActionBottomDialog"
        )
    }

    override fun onStop() {
        //viewModel.getLiveDataNowDate().obser
        super.onStop()
    }

    /*
    override fun onDateComplete(time: Calendar, viewCode: Int) {
        viewModel.setDateEvent(time)
    }*/
}