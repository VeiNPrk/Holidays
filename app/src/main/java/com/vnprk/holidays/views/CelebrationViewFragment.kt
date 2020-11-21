package com.vnprk.holidays.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.vnprk.holidays.BR
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vnprk.holidays.R
import com.vnprk.holidays.databinding.FragmentHolidayBinding
import com.vnprk.holidays.viewmodels.HolidayViewModel
import com.vnprk.holidays.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.rv_holiday_maket.view.*

class CelebrationViewFragment : BottomSheetDialogFragment() {

    private var idHoliday = 0
    private lateinit var binding: FragmentHolidayBinding
    private val viewModel: HolidayViewModel by activityViewModels()
    private val args :CelebrationViewFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        idHoliday = args.idEvent//arguments?.getInt(KEY_ID_HOLIDAY, 0)!!
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        dialog!!.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            /*val coordinatorLayout =
                d.findViewById<View>(R.id.locUXCoordinatorLayout) as CoordinatorLayout?*/
            val bottomSheetInternal = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
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
            holiday?.let { itHoliday->
                binding.setVariable(BR.holiday, itHoliday)
                binding.executePendingBindings()
                itHoliday.img?.let { imgLink->
                        Glide
                            .with(this)
                            .load(imgLink)
                            .error(R.drawable.ic_baseline_today_50)
                            .placeholder(R.drawable.ic_baseline_today_50)
                            .into(binding.imvHoliday)
                }
                itHoliday.link?.let { link ->
                    binding.imvOpenLink.setOnClickListener {
                        openWebPage(link)
                    }
                }
            }
        })
    }

    private fun openWebPage(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }
}