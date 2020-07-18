package com.vnprk.holidays.utils

import androidx.recyclerview.widget.RecyclerView
import com.vnprk.holidays.BR
import com.vnprk.holidays.databinding.RvHolidayMaketBinding
import com.vnprk.holidays.models.Event
import com.vnprk.holidays.models.Holiday

class HolidayViewHolder(var binding: RvHolidayMaketBinding) :
    RecyclerView.ViewHolder(binding.root), EventsRecyclerAdapter.Binder {
    override fun bind(event: Event/*, detailClickListener: EventsRecyclerAdapter.ControlListClickListener*/) {
        binding.setVariable(BR.holiday, (event as Holiday))
        //binding.dateUtils = DateUtils.Companion
        binding.executePendingBindings()
       /* binding.root.setOnClickListener {
            detailClickListener.onClick(detailControl.id)
        }*/
    }
}