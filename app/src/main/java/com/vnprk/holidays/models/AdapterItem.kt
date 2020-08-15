package com.vnprk.holidays.models

data class AdapterItem<out T>(
    val value: T?,
    val viewType: Int,
    val typeHeader: Int,
    val nameHeader: String,
    val isVisibleMore:Boolean
)