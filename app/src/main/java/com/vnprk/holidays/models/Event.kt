package com.vnprk.holidays.models

open class Event(
    open var idEvent: Int,
    open var nameEvent: String,
    open var descriptEvent: String,
    open var imgEvent: String?
){
    companion object {
        const val HOLIDAY_TYPE = 0
        const val PRIVATE_TYPE = 100
    }
}