package com.vnprk.holidays.utils

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.vnprk.holidays.BR
import com.vnprk.holidays.databinding.RvHeaderMaketBinding
import com.vnprk.holidays.models.AdapterItem
import com.vnprk.holidays.models.Event
import com.vnprk.holidays.models.PrivateEvent

class HeaderViewHolder(var binding: RvHeaderMaketBinding) :
    RecyclerView.ViewHolder(binding.root), EventsRecyclerAdapter.Binder {
    override fun bind(context: Context, item: AdapterItem<Event>, detailClickListener: EventsRecyclerAdapter.EventDetailClickListener?, moreClickListener: EventsRecyclerAdapter.EventMoreClickListener?) {
        binding.setVariable(BR.adapterItem, item)
        //binding.dateUtils = DateUtils.Companion
        binding.executePendingBindings()
        moreClickListener?.let{ moreClick->
            binding.root.setOnClickListener {
                moreClick.onMoreClick(item.typeHeader)
            }
        }
    }
}