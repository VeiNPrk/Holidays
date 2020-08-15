package com.vnprk.holidays.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vnprk.holidays.BR
import com.vnprk.holidays.R
import com.vnprk.holidays.databinding.FragmentViewPrivateBinding
import com.vnprk.holidays.models.Event
import com.vnprk.holidays.viewmodels.PrivateEventViewModel


class PrivateEventViewFragment : BottomSheetDialogFragment() {

    var idEvent = 0
    lateinit var binding: FragmentViewPrivateBinding
    private val viewModel: PrivateEventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        idEvent = arguments?.getInt(KEY_ID_EVENT, 0)!!
        //dialog!!.window.setLayout(300 /*our width*/, ViewGroup.LayoutParams.MATCH_PARENT)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        dialog!!.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheetInternal = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val bottomSheetBehavior: BottomSheetBehavior<*> =
                BottomSheetBehavior.from<View>(bottomSheetInternal)
            bottomSheetBehavior.peekHeight =
                bottomSheetInternal.getHeight()
            bottomSheetBehavior.setPeekHeight(bottomSheetInternal.getHeight())
        }
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_view_private, container, false)
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
        binding.fabEdit.setOnClickListener {
            val navController =
                activity?.let { it1 -> Navigation.findNavController(it1, R.id.nav_host_fragment) }
            /*var action = PrivateListFragmentDirections.actionNavCelebrationPrivateToPrivateEventEditFragment(idEvent)
            navController?.navigate(action)*/
            val bundleToTransfer = Bundle()
            bundleToTransfer.putInt("key_id_event", idEvent)
            navController?.navigate(R.id.privateEventEditFragment, bundleToTransfer)
            dismiss()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getPrivateEvent(idEvent).observe(this, Observer { holiday ->
            holiday?.let {
                binding.setVariable(BR.event, it)
                binding.executePendingBindings()
            }
        })
    }

    companion object {
        const val KEY_ID_EVENT = "id_event_key"
        fun newInstance(idHoliday:Int): PrivateEventViewFragment {
            val myFragment = PrivateEventViewFragment()
            val args = Bundle()
            args.putInt(KEY_ID_EVENT, idHoliday)
            myFragment.arguments = args
            return myFragment
        }
    }
}