package com.vnprk.holidays.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vnprk.holidays.models.AdapterItem
import com.vnprk.holidays.models.Event
import com.vnprk.holidays.models.Holiday
import com.vnprk.holidays.models.PrivateEvent
import com.vnprk.holidays.utils.ViewHolderFactory


class EventsRecyclerAdapter(val detailClickListener: EventDetailClickListener?, val moreClickListener: EventMoreClickListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    //lateinit var context:Context
    //private lateinit var detailControlClickListener : DetailControlClickListener;
    var data:List<AdapterItem<Event>> = ArrayList<AdapterItem<Event>>()

    interface EventDetailClickListener {
        fun onDetailClick(idDetail : Int, type : Int)
        //void onDeleteClick(PictureClass picture);
    }

    interface EventMoreClickListener {
        fun onMoreClick(type : Int)
        //void onDeleteClick(PictureClass picture);
    }

    fun setDetailsData(details: List<AdapterItem<Event>>){
        data=details
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return getViewHolder(inflater, parent, viewType)

        /*val inflater = LayoutInflater.from(parent.context)
        val binding: RvDetailMaketBinding = DataBindingUtil.inflate(inflater, maket, parent, false)
        val viewHolder = ControlListViewHolder(binding)
        return viewHolder*/
    }

    override fun onBindViewHolder(holder : RecyclerView.ViewHolder, i : Int ) {
        (holder as Binder).bind(data.get(i), detailClickListener, moreClickListener)
         //val detail = data.get(i);
        /*holder.bind(data.get(i))
        holder.binding.root.setOnClickListener {
            detailControlClickListener.onClick(data.get(i).id)
        }*/
    }

    override fun getItemViewType(position: Int): Int =
        if(data.get(position).viewType == TYPE_ITEM )
            when {
                data.get(position).value is Holiday -> {
                    Event.HOLIDAY_TYPE;
                }
                data.get(position).value is PrivateEvent -> {
                    Event.PRIVATE_TYPE;
                }
                else -> {
                    -1;
                }
            }
        else data.get(position).viewType


    /*protected abstract fun getLayoutId(position: Int, obj: Detail): Int*/

    protected open fun getViewHolder(inflater:LayoutInflater, parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolderFactory.create(inflater, parent, viewType)
    }

    internal interface Binder {
        fun bind(detailControl: AdapterItem<Event>, detailClickListener: EventDetailClickListener?, moreClickListener: EventMoreClickListener?)
    }

    override fun getItemCount() = data.size

    companion object {
        const val TYPE_ITEM = 0
        const val TYPE_HEADER = 1
    }


}

