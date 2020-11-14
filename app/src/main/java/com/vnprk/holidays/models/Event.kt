package com.vnprk.holidays.models

import android.R

/*
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
}*/

interface Event {
    var id: Int
    var name: String?
    var descript: String?
    var img: String?
    var type: Int

    companion object {
        const val HOLIDAY_TYPE = 10
        const val HOLIDAY_TYPE_CEL = 0
        const val HOLIDAY_TYPE_COUNTRY = 1
        const val HOLIDAY_TYPE_RELIGIOUS = 2
        const val HOLIDAY_TYPE_OTHER = 3
        const val PRIVATE_TYPE = 100

        val HOLIDAY_TYPES = mapOf(HOLIDAY_TYPE_CEL to "Праздники", HOLIDAY_TYPE_COUNTRY to "Национальные праздники", HOLIDAY_TYPE_RELIGIOUS to "Религиозные праздники", HOLIDAY_TYPE_OTHER to "Разное")
        val periods = mapOf(
            0 to "Не повторять",
            1 to "Каждый день",
            2 to "Каждую неделю",
            3 to "Каждый месяц",
            4 to "Каждый год")//.toList()
        val notifys = mapOf(
            0 to "Нет",
            1 to "За 5 минут",
            2 to "За 15 минут",
            3 to "За 1 час",
            4 to "За 6 часов",
            5 to "За 1 день",
            6 to "За 1 неделю",
            7 to "За 1 месяц")
        val notifysMin = mapOf(
            0 to 0,
            1 to 5,
            2 to 15,
            3 to 60,
            4 to 360,
            5 to 1440,
            6 to 10080,
            7 to 40320)
        val colors = mapOf(
            0 to "#f44336",
            1 to "#e91e63",
            2 to "#9c27b0",
            3 to "#673ab7",
            4 to "#3f51b5",
            5 to "#2196f3",
            6 to "#4caf50",
            7 to "#8bc34a",
            8 to "#ffeb3b",
            9 to "#ff9800")
    }
}