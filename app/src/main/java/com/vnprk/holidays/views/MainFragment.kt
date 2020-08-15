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
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vnprk.holidays.R
import com.vnprk.holidays.models.Event
import com.vnprk.holidays.models.Status
import com.vnprk.holidays.utils.EventsRecyclerAdapter
import com.vnprk.holidays.utils.NetworkUtils
import com.vnprk.holidays.utils.NetworkUtils.Companion.networkState
import com.vnprk.holidays.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_private_list.view.*

class MainFragment : Fragment(), EventsRecyclerAdapter.EventDetailClickListener, EventsRecyclerAdapter.EventMoreClickListener {

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
        rv = root.findViewById(R.id.rv_all_events)
        imvNoConnect = root.findViewById(R.id.imv_no_connect)
        tvMessage = root.tv_msg
        swipeLayout = root.findViewById(R.id.swipe_refresh_layout)
        swipeLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        swipeLayout.setOnRefreshListener {
            Log.i("ControlList", "onRefresh called from SwipeRefreshLayout")
            isSwipe = true
            tvMessage.visibility=View.GONE
            viewModel.onRefreshLayout()
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
        viewModel.getAllEvents().observe(viewLifecycleOwner, Observer {
            mAdapter.setDetailsData(it)
            swipeLayout.isRefreshing=false
            if(it.isEmpty())
            {
                tvMessage.text=getString(R.string.no_have_celebrate)
                tvMessage.visibility=View.VISIBLE
            }
            else tvMessage.visibility=View.GONE
        })
        /*viewModel.getTest2().observe(viewLifecycleOwner, Observer {
            mAdapter.setDetailsData(it)
        })*/
        viewModel.refreshData()
        return root
    }

    fun setRecyclerView() {
        val mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //val maket = viewModel.getRvMaket(typeDetail)
        mAdapter = //viewModel.getRvAdapter(typeDetail, this)//
            context?.let { EventsRecyclerAdapter(this, this) }!!
        rv.setHasFixedSize(true)
        rv.layoutManager = mLayoutManager
        rv.adapter = mAdapter
        rv.itemAnimator = DefaultItemAnimator()
    }

    override fun onDetailClick(idEvent: Int, type:Int) {
        val bottomDialogFragment = if(type == Event.PRIVATE_TYPE){
            //val addPhotoBottomDialogFragment: PrivateEventViewFragment =
                PrivateEventViewFragment.newInstance(idEvent)
            /*addPhotoBottomDialogFragment.show(
                parentFragmentManager,
                "ActionBottomDialog"
            )*/
        }
        else{
            //val addPhotoBottomDialogFragment: CelebrationViewFragment =
                CelebrationViewFragment.newInstance(idEvent)
            /*addPhotoBottomDialogFragment.show(
                parentFragmentManager,
                "ActionBottomDialog"
            )*/
        }
        bottomDialogFragment
        bottomDialogFragment.show(
            parentFragmentManager,
            "ActionBottomDialog"
        )
    }

    override fun onMoreClick(type: Int) {
       /* when {
            isTablet -> {*/
                val navHostFragment = parentFragment?.parentFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                /*val bundleToTransfer = Bundle()
                bundleToTransfer.putInt("key_id_detail", 0)
                bundleToTransfer.putInt("key_type_detail", typeDetail)
                val builder = NavOptions.Builder()
                val navOptions =
                    builder.setEnterAnim(R.anim.slide_in_right).setExitAnim(R.anim.scale_fade_out)
                        .build()*/
        var action = viewModel.getNavigationAction(type)

            //action =
                navHostFragment.navController.navigate(action)
           /* }
            else -> {
                //val action = ControlListFragmentDirections.actionControlListFragmentToDetailCastEditFragment(0, typeDetail)
                val action = viewModel.getDetailClickAction(typeDetail, 0, true)
                mview.let { Navigation.findNavController(it).navigate(action) }
            }*/
        //}
        //Navigation.findNavController(it).navigate(MainFragmentDirections.actionNavHomeToNavCelebration(type))
    }
}
