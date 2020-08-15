package com.vnprk.holidays.utils

import androidx.recyclerview.widget.RecyclerView
import com.vnprk.holidays.BR
import com.vnprk.holidays.databinding.RvHolidayMaketBinding
import com.vnprk.holidays.models.AdapterItem
import com.vnprk.holidays.models.Event
import com.vnprk.holidays.models.Holiday

class HolidayViewHolder(var binding: RvHolidayMaketBinding) :
    RecyclerView.ViewHolder(binding.root), EventsRecyclerAdapter.Binder {
    override fun bind(item: AdapterItem<Event>, detailClickListener: EventsRecyclerAdapter.EventDetailClickListener?, moreClickListener: EventsRecyclerAdapter.EventMoreClickListener?) {
        binding.setVariable(BR.holiday, (item.value as Holiday))
        binding.setVariable(BR.adapterItem, item)
        //binding.dateUtils = DateUtils.Companion
        binding.executePendingBindings()
        detailClickListener?.let{ detailClick->
            binding.root.setOnClickListener {
                detailClick.onDetailClick(item.value.id, item.typeHeader)
            }
        }
        moreClickListener?.let{ moreClick->
            binding.btnMore.setOnClickListener {
                moreClick.onMoreClick(item.typeHeader)
            }
        }
    }
}