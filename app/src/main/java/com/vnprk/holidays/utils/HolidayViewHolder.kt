package com.vnprk.holidays.utils

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vnprk.holidays.BR
import com.vnprk.holidays.R
import com.vnprk.holidays.databinding.RvHolidayMaketBinding
import com.vnprk.holidays.models.AdapterItem
import com.vnprk.holidays.models.Event
import com.vnprk.holidays.models.Holiday
import kotlinx.android.synthetic.main.rv_holiday_maket.view.*

class HolidayViewHolder(var binding: RvHolidayMaketBinding) :
    RecyclerView.ViewHolder(binding.root), EventsRecyclerAdapter.Binder {
    override fun bind(context: Context, item: AdapterItem<Event>, detailClickListener: EventsRecyclerAdapter.EventDetailClickListener?, moreClickListener: EventsRecyclerAdapter.EventMoreClickListener?) {
        binding.setVariable(BR.holiday, (item.value as Holiday))
        binding.setVariable(BR.adapterItem, item)
        //binding.dateUtils = DateUtils.Companion
        binding.executePendingBindings()
        (item.value as Holiday).img?.let {
            Glide
                .with(context)
                .load(it)
                .error(R.drawable.ic_baseline_today_50)
                .placeholder(R.drawable.ic_baseline_today_50)
                .into(binding.imvHolidayMaket)
        }
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