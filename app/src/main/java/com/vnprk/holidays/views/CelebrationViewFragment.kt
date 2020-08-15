package com.vnprk.holidays.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.vnprk.holidays.BR
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vnprk.holidays.R
import com.vnprk.holidays.databinding.FragmentHolidayBinding
import com.vnprk.holidays.viewmodels.HolidayViewModel
import com.vnprk.holidays.viewmodels.MainViewModel


class CelebrationViewFragment : BottomSheetDialogFragment() {

    var idHoliday = 0
    lateinit var binding: FragmentHolidayBinding
    private val viewModel: HolidayViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        idHoliday = arguments?.getInt(KEY_ID_HOLIDAY, 0)!!
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        dialog!!.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            /*val coordinatorLayout =
                d.findViewById<View>(R.id.locUXCoordinatorLayout) as CoordinatorLayout?*/
            val bottomSheetInternal = d!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val bottomSheetBehavior: BottomSheetBehavior<*> =
                BottomSheetBehavior.from<View>(bottomSheetInternal)
            // val bottomSheetInternal = d.findViewById<View>(R.id.locUXView)
            /*val bottomSheetBehavior =
                BottomSheetBehavior.from<View>(bottomSheetInternal!!)*/
            //bottomSheetBehavior.isHideable=false
            bottomSheetBehavior.peekHeight =
                bottomSheetInternal.getHeight()
            bottomSheetBehavior.setPeekHeight(bottomSheetInternal.getHeight())
            //coordinatorLayout.getParent().requestLayout()
        }
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_holiday, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getHoliday(idHoliday).observe(this, Observer { holiday ->
            holiday?.let {
                binding.setVariable(BR.holiday, it)
                binding.executePendingBindings()
            }
        })
    }

    companion object {
        const val KEY_ID_HOLIDAY = "id_holiday_key"
        fun newInstance(idHoliday:Int): CelebrationViewFragment {
            val myFragment = CelebrationViewFragment()
            val args = Bundle()
            args.putInt(KEY_ID_HOLIDAY, idHoliday)
            myFragment.arguments = args
            return myFragment
        }
    }
}