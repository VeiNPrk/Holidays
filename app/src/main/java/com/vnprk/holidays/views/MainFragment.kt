package com.vnprk.holidays.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.fmklab.fmkinp.models.Status
import com.vnprk.holidays.R
import com.vnprk.holidays.utils.EventsRecyclerAdapter
import com.vnprk.holidays.utils.NetworkUtils
import com.vnprk.holidays.utils.NetworkUtils.Companion.networkState
import com.vnprk.holidays.viewmodels.MainViewModel

class MainFragment : Fragment() {

    private var isSwipe = false
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var mAdapter: EventsRecyclerAdapter
    private lateinit var rv: RecyclerView
    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var imvNoConnect: ImageView
    private lateinit var tvMessage: TextView
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        rv = root.findViewById(R.id.rv_all_events)
        imvNoConnect = root.findViewById(R.id.imv_no_connect)
        tvMessage = root.findViewById(R.id.tv_msg)
        swipeLayout = root.findViewById(R.id.swipe_refresh_layout)
        swipeLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        swipeLayout.setOnRefreshListener {
            Log.i("ControlList", "onRefresh called from SwipeRefreshLayout")
            isSwipe = true
            //viewModel.refreshData(myUserId, myEntId)
        }
        setRecyclerView()
        networkState.observe(viewLifecycleOwner, Observer { nState ->
            networkState.let {
                when (nState.status) {
                    Status.RUNNING -> {
                        Log.d("coroutine", "Status.RUNNING")
                        swipeLayout.isRefreshing=true
                        imvNoConnect.visibility=View.GONE
                        tvMessage.visibility=View.GONE
                        /*isVisibleProgress = true
                        message = nState.msg*/
                    }
                    Status.SUCCESS -> {
                        Log.d("coroutine", "Status.SUCCESS")
                        swipeLayout.isRefreshing=false
                        imvNoConnect.visibility=View.GONE
                        tvMessage.visibility=View.GONE
                        //isVisibleProgress = false
                    }
                    Status.FAILED -> {
                        Log.d("coroutine", "Status.FAILED")
                        swipeLayout.isRefreshing=false
                        tvMessage.visibility=View.VISIBLE
                        if(nState.msg==NetworkUtils.ERROR_NO_CONNECTION){
                            imvNoConnect.visibility=View.VISIBLE
                            tvMessage.text=getString(R.string.no_first_connection)
                        }else{
                            tvMessage.text=nState.msg
                        }
                    }
                }
            }
        })
       /* viewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        viewModel.getTestHoliday().observe(viewLifecycleOwner, Observer {
            mAdapter.setDetailsData(it)
        })
        viewModel.refreshData()
        return root
    }

    fun setRecyclerView() {
        val mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //val maket = viewModel.getRvMaket(typeDetail)
        mAdapter = //viewModel.getRvAdapter(typeDetail, this)//
            context?.let { EventsRecyclerAdapter(/*maket, this*/) }!!
        rv.setHasFixedSize(true)
        rv.layoutManager = mLayoutManager
        rv.adapter = mAdapter

        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        rv.addItemDecoration(itemDecoration)
        rv.itemAnimator = DefaultItemAnimator()
    }
}
