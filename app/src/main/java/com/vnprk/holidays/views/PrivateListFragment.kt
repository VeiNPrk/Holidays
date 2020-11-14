package com.vnprk.holidays.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vnprk.holidays.R
import com.vnprk.holidays.utils.EventsRecyclerAdapter
import com.vnprk.holidays.viewmodels.PrivateListViewModel
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_private_list.view.*
import kotlinx.android.synthetic.main.fragment_private_list.view.tv_msg

class PrivateListFragment : Fragment() , EventsRecyclerAdapter.EventDetailClickListener {

    private val viewModel: PrivateListViewModel by activityViewModels()
    private lateinit var mAdapter: EventsRecyclerAdapter
    private lateinit var rv: RecyclerView
    private lateinit var fab : FloatingActionButton
    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var tvMessage: TextView
    private var isSwipe = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_private_list, container, false)
        tvMessage = root.tv_msg
        rv = root.rv_private_events
        swipeLayout = root.findViewById(R.id.swipe_refresh_layout)
        swipeLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        swipeLayout.setOnRefreshListener {
            Log.i("ControlList", "onRefresh called from SwipeRefreshLayout")
            isSwipe = true
            tvMessage.visibility=View.GONE
            viewModel.onRefreshLayout()
        }
        fab = root.fab_add_event
        fab.setOnClickListener(View.OnClickListener {
            val navController =
                activity?.let { it1 -> Navigation.findNavController(it1, R.id.nav_host_fragment) }
            val action = PrivateListFragmentDirections.actionNavCelebrationPrivateToPrivateEventEditFragment()
            navController?.navigate(action)
        })
        setRecyclerView()
        viewModel.getPrivateEvents().observe(viewLifecycleOwner, Observer {
            swipeLayout.isRefreshing=false
            mAdapter.setDetailsData(it)
            if(it.isEmpty())
            {
                tvMessage.text=getString(R.string.no_have_private)
                tvMessage.visibility=View.VISIBLE
            }
            else tvMessage.visibility=View.GONE
        })
        return root
    }

    private fun setRecyclerView() {
        val mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mAdapter = //viewModel.getRvAdapter(typeDetail, this)//
            context?.let { EventsRecyclerAdapter(requireContext(),this, null) }!!
        rv.setHasFixedSize(true)
        rv.layoutManager = mLayoutManager
        rv.adapter = mAdapter
        rv.itemAnimator = DefaultItemAnimator()
    }

    override fun onDetailClick(idEvent: Int, type:Int) {
        val bottomDialogFragment =
            PrivateEventViewFragment.newInstance(idEvent)
        bottomDialogFragment
        bottomDialogFragment.show(
            parentFragmentManager,
            "ActionBottomDialog"
        )
    }
}