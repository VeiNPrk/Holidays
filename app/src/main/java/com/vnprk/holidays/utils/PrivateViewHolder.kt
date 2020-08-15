package com.vnprk.holidays.utils

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.recyclerview.widget.RecyclerView
import com.vnprk.holidays.BR
import com.vnprk.holidays.R
import com.vnprk.holidays.databinding.RvPrivateMaketBinding
import com.vnprk.holidays.models.AdapterItem
import com.vnprk.holidays.models.Event
import com.vnprk.holidays.models.PrivateEvent


class PrivateViewHolder(var binding: RvPrivateMaketBinding) :
    RecyclerView.ViewHolder(binding.root), EventsRecyclerAdapter.Binder {
    override fun bind(item: AdapterItem<Event>, detailClickListener: EventsRecyclerAdapter.EventDetailClickListener?, moreClickListener: EventsRecyclerAdapter.EventMoreClickListener?) {
        binding.setVariable(BR.event, (item.value as PrivateEvent))
        binding.setVariable(BR.adapterItem, item)
        //binding.dateUtils = DateUtils.Companion
        binding.executePendingBindings()
        //binding.root.setBackgroundResource(R.drawable.test_gradient)
        val gd = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(Color.WHITE,
                //Color.WHITE,
                Color.parseColor(Event.colors.get(item.value.idColor)),
                Color.parseColor(Event.colors.get(item.value.idColor)))
        )
        gd.cornerRadius = 0f
        //gd.setGradientCenter(0.9f, 0.9f)
        binding.layoutGradient.background=gd
        //binding.root.background=gd
        detailClickListener?.let{ detailClick->
            binding.root.setOnClickListener {
                detailClick.onDetailClick(item.value.id, item.typeHeader)
            }
        }
        moreClickListener?.let { moreClick ->
            binding.btnMore.setOnClickListener {
                moreClick.onMoreClick(item.typeHeader)
            }
        }
    }
}