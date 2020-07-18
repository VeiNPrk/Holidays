package com.vnprk.holidays.utils

import androidx.recyclerview.widget.RecyclerView
import com.vnprk.holidays.BR
import com.vnprk.holidays.utils.EventsRecyclerAdapter
import com.vnprk.holidays.databinding.RvPrivateMaketBinding
import com.vnprk.holidays.models.Event
import com.vnprk.holidays.models.PrivateEvent

class PrivateViewHolder(var binding: RvPrivateMaketBinding) :
    RecyclerView.ViewHolder(binding.root), EventsRecyclerAdapter.Binder {
    override fun bind(event: Event/*, detailClickListener: EventsRecyclerAdapter.ControlListClickListener*/) {
        binding.setVariable(BR.event, (event as PrivateEvent))
        //binding.dateUtils = DateUtils.Companion
        binding.executePendingBindings()
       /* binding.root.setOnClickListener {
            detailClickListener.onClick(detailControl.id)
        }*/
    }
}