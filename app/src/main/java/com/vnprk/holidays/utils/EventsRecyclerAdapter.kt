package com.vnprk.holidays.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vnprk.holidays.models.Event
import com.vnprk.holidays.models.Holiday
import com.vnprk.holidays.models.PrivateEvent
import com.vnprk.holidays.utils.ViewHolderFactory


class EventsRecyclerAdapter(/*val maket:Int, val detailControlClickListener: ControlListClickListener*/) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    //lateinit var context:Context
    //private lateinit var detailControlClickListener : DetailControlClickListener;
    var data:List<Event> = ArrayList<Event>()
    interface ControlListClickListener {
        fun onClick(idDetail : Int)
        //void onDeleteClick(PictureClass picture);
    }

    fun setDetailsData(details: List<Event>){
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
        (holder as Binder).bind(data.get(i)/*, detailControlClickListener*/)
         //val detail = data.get(i);
        /*holder.bind(data.get(i))
        holder.binding.root.setOnClickListener {
            detailControlClickListener.onClick(data.get(i).id)
        }*/
    }

    override fun getItemViewType(position: Int): Int = when {
        data.get(position) is Holiday -> {
            Event.HOLIDAY_TYPE;
        }
        data.get(position) is PrivateEvent -> {
            Event.PRIVATE_TYPE;
        }
        else -> {
            -1;
        }
    }

    /*protected abstract fun getLayoutId(position: Int, obj: Detail): Int*/

    protected open fun getViewHolder(inflater:LayoutInflater, parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolderFactory.create(inflater, parent, viewType)
    }

    internal interface Binder {
        fun bind(detailControl: Event/*, detailClickListener: ControlListClickListener*/)
    }

    override fun getItemCount() = data.size


}

