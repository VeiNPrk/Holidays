package com.vnprk.holidays.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.vnprk.holidays.R
import com.vnprk.holidays.databinding.RvHolidayMaketBinding
import com.vnprk.holidays.databinding.RvPrivateMaketBinding
import com.vnprk.holidays.models.Event

object ViewHolderFactory {
    fun create(inflater:LayoutInflater, parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Event.HOLIDAY_TYPE -> {
                val binding: RvHolidayMaketBinding =
                    DataBindingUtil.inflate(inflater, R.layout.rv_holiday_maket, parent, false)
                HolidayViewHolder(binding)
            }
            Event.PRIVATE_TYPE -> {
                val binding: RvPrivateMaketBinding =
                    DataBindingUtil.inflate(inflater, R.layout.rv_private_maket, parent, false)
                PrivateViewHolder(binding)}
            else -> throw Exception("Wrong view type")
        }
    }
}



